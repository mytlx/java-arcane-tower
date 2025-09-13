package com.mytlx.handcraft.rpc.client;

import com.mytlx.handcraft.rpc.model.RemoteService;
import com.mytlx.handcraft.rpc.model.RpcMethod;
import com.mytlx.handcraft.rpc.model.RpcMethodDescriptor;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 16:45:48
 */
@Slf4j
public class RpcClient implements SmartInitializingSingleton {

    private final NioEventLoopGroup worker = new NioEventLoopGroup();

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

    @Value("${handcraft.rpc.server.host}")
    private String host;

    @Value("${handcraft.rpc.server.port}")
    private int port;

    @PostConstruct
    public void init() {
        log.info("rpc client init");
        // new Thread(this::connect).start();
    }

    public void connect() {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                        }
                    });
            ChannelFuture cf = bootstrap.connect(host, port)
                    .addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            this.channel = future.channel();
                        } else {
                            log.error("Failed to connect to server", future.cause());
                            reconnect();
                        }
                    });

            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("Failed to connect to server", e);
            throw new RuntimeException(e);
        } finally {
            worker.shutdownGracefully();
        }

    }

    public void reconnect() {

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
}
