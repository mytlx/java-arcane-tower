package com.mytlx.handcraft.rpc.client;

import com.mytlx.handcraft.rpc.handler.ClientHeartbeatHandler;
import com.mytlx.handcraft.rpc.handler.JsonCallMessageEncoder;
import com.mytlx.handcraft.rpc.handler.JsonMessageDecoder;
import com.mytlx.handcraft.rpc.handler.RpcClientMessageHandler;
import com.mytlx.handcraft.rpc.model.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 16:45:48
 */
@Slf4j
public class RpcClient implements SmartInitializingSingleton, ApplicationContextAware {

    private final NioEventLoopGroup worker = new NioEventLoopGroup();

    private ApplicationContext applicationContext;

    @Setter
    @Getter
    private String clientId;

    // 扫描的包，里面都是暴露的RPC服务
    @Setter
    @Getter
    private String[] scanPackages;

    private Channel channel;

    // className -> (methodId -> methodDesc)
    private final Map<String, Map<String, RpcMethodDescriptor>> classMethodDescMap = new HashMap<>();
    // methodId -> method
    private final Map<String, Method> reflectMethodMap = new HashMap<>();
    // requestId -> future<response>
    private final Map<String, CompletableFuture<MessagePayload.RpcResponse>> requestFutureMap = new ConcurrentHashMap<>();

    @Value("${handcraft.rpc.server.host}")
    private String host;

    @Value("${handcraft.rpc.server.port}")
    private int port;

    @PostConstruct
    public void init() {
        log.info("rpc client init");
        new Thread(this::connect).start();
    }

    public void connect() {
        try {
            RpcClientMessageHandler rpcClientMessageHandler = new RpcClientMessageHandler(this);
            ClientHeartbeatHandler clientHeartbeatHandler = new ClientHeartbeatHandler(this);

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new LoggingHandler())
                                    .addLast(new JsonCallMessageEncoder())
                                    .addLast(new JsonMessageDecoder())
                                    .addLast(new IdleStateHandler(0, 5, 0))
                                    .addLast(clientHeartbeatHandler)
                                    .addLast(rpcClientMessageHandler)
                            ;
                            ch.pipeline().addLast(new ChannelDuplexHandler() {
                                @Override
                                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                    promise.addListener(future -> {
                                        if (!future.isSuccess()) {
                                            log.error("消息发送失败: {}", msg);
                                            log.error(future.cause().getMessage(), future.cause());
                                        }
                                    });
                                    super.write(ctx, msg, promise);
                                }
                            });
                        }
                    });
            ChannelFuture cf = bootstrap.connect(host, port)
                    .addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            log.info("client id: {} 连接成功", clientId);
                            this.channel = future.channel();
                            // 连接成功，发送注册消息
                            sendRegistrationRequest();
                        } else {
                            log.error("Failed to connect to server", future.cause());
                            reconnect();
                        }
                    });

            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("Failed to connect to rpc server", e);
            throw new RuntimeException(e);
        } finally {
            worker.shutdownGracefully();
        }
    }

    public void reconnect() {
        worker.schedule(RpcClient.this::connect, 5, TimeUnit.SECONDS);
    }

    public void sendRegistrationRequest() {
        MessagePayload messagePayload = new MessagePayload()
                .setClientId(clientId)
                .setMessageType(MessageTypeEnum.REGISTER);
        channel.writeAndFlush(messagePayload);
    }

    public void handleResponse(MessagePayload.RpcResponse response) {
        String requestId = response.getRequestId();
        CompletableFuture<MessagePayload.RpcResponse> future = requestFutureMap.get(requestId);
        future.complete(response);
    }

    public void handleRequest(MessagePayload.RpcRequest request) {
        Class<?> requestClazz = null;
        try {
            // interface: BookingService
            requestClazz = Class.forName(request.getRequestClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // implementation class: BookingServiceImpl
        Map<String, ?> beans = applicationContext.getBeansOfType(requestClazz);

        if (beans.size() > 1) {
            log.error("more than one bean of type: {}", requestClazz);
            throw new RuntimeException("more than one bean of type: " + requestClazz);
        }

        // 实现类的对象，真正的服务提供者
        Object requestClassBean = beans.values().iterator().next();

        String className = requestClassBean.getClass().getName();
        Map<String, RpcMethodDescriptor> methodMdMap = classMethodDescMap.get(className);

        String methodId = RpcMethodDescriptor.generateMethodId(
                request.getRequestMethodSimpleName(), request.getParameterTypes(), request.getReturnValueType());
        RpcMethodDescriptor md = methodMdMap.get(methodId);
        if (md == null) {
            throw new RuntimeException("no such method: " + methodId);
        }

        // tlxTODO: validate method
        Method method = reflectMethodMap.get(methodId);
        Object result = null;
        try {
            result = method.invoke(requestClassBean, request.getParameters());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        MessagePayload response = new MessagePayload()
                .setClientId(clientId)
                .setMessageType(MessageTypeEnum.RESPONSE)
                .setPayload(
                        new MessagePayload.RpcResponse()
                                .setRequestId(request.getRequestId())
                                .setReturnValue(result)
                );
        channel.writeAndFlush(response);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz, String requestClientId) {
        return (T) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{clazz},
                (proxy, method, args) -> {

                    // 处理 Object 自带方法
                    if (method.getDeclaringClass() == Object.class) {
                        return switch (method.getName()) {
                            case "equals" -> proxy == args[0];
                            case "hashCode" -> System.identityHashCode(proxy);
                            case "toString" ->
                                    proxy.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(proxy));
                            default -> throw new IllegalStateException("Unexpected Object method: " + method);
                        };
                    }

                    String requestId = UUID.randomUUID().toString();
                    MessagePayload msg = new MessagePayload()
                            .setClientId(clientId)
                            .setMessageType(MessageTypeEnum.CALL)
                            .setPayload(
                                    new MessagePayload.RpcRequest()
                                            .setRequestClientId(requestClientId)
                                            .setRequestId(requestId)
                                            .setRequestMethodSimpleName(method.getName())
                                            .setRequestClassName(method.getDeclaringClass().getName())
                                            .setReturnValueType(method.getReturnType().getName())
                                            .setParameterTypes(Arrays.stream(method.getParameterTypes()).map(Class::getName).toArray(String[]::new))
                                            .setParameters(args)
                            );

                    CompletableFuture<MessagePayload.RpcResponse> future = new CompletableFuture<>();
                    requestFutureMap.put(requestId, future);

                    log.debug("before writeAndFlush");
                    channel.writeAndFlush(msg);
                    log.debug("after writeAndFlush: {}", msg);

                    try {
                        MessagePayload.RpcResponse response = future.get(10, TimeUnit.SECONDS);

                        requestFutureMap.remove(requestId);

                        return response.getReturnValue();
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        return "超时";
                    }
                }
        );
    }

    @Override
    public void afterSingletonsInstantiated() {
        log.info("RPC methods scanning started");

        List<Class<?>> rpcClassList = new ArrayList<>();
        for (String sp : scanPackages) {
            sp = sp.replace("\\.", "/");
            ClassPathScanningCandidateComponentProvider scanner =
                    new ClassPathScanningCandidateComponentProvider(false);
            scanner.addIncludeFilter(new AssignableTypeFilter(Object.class));

            Set<BeanDefinition> candidates = scanner.findCandidateComponents(sp);
            for (BeanDefinition bd : candidates) {
                try {
                    Class<?> scannedClass = Class.forName(bd.getBeanClassName());
                    if (RemoteService.class.isAssignableFrom(scannedClass)) {
                        rpcClassList.add(scannedClass);
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // rpcClassList 已经包含了所有 RemoteService 的子类
        // 找出哪些方法允许被远程调用 @RpcMethod
        for (Class<?> clazz : rpcClassList) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RpcMethod.class)) {
                    RpcMethodDescriptor md = RpcMethodDescriptor.build(method);
                    classMethodDescMap.computeIfAbsent(md.getClassName(), k -> new HashMap<>())
                            .put(md.getMethodId(), md);
                    reflectMethodMap.put(md.getMethodId(), method);
                }
            }
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext ctx) throws BeansException {
        this.applicationContext = ctx;
    }
}
