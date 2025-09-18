package com.mytlx.handcraft.rpc.client;

import com.mytlx.handcraft.rpc.model.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-18 13:58:59
 */
@Slf4j
public abstract class AbstractRemoteClient implements RemoteClient, SmartInitializingSingleton, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Setter
    @Getter
    private String clientId;

    // 扫描的包，里面都是暴露的RPC服务
    @Setter
    @Getter
    private String[] scanPackages;

    // className -> (methodId -> methodDesc)
    private final Map<String, Map<String, RpcMethodDescriptor>> classMethodDescMap = new HashMap<>();
    // methodId -> method
    private final Map<String, Method> reflectMethodMap = new HashMap<>();
    // requestId -> future<response>
    private final Map<String, CompletableFuture<MessagePayload.RpcResponse>> requestFutureMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(@NonNull ApplicationContext ctx) throws BeansException {
        this.applicationContext = ctx;
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
    public void doRequest(MessagePayload msg, String requestId, CompletableFuture<MessagePayload.RpcResponse> future) {
        requestFutureMap.put(requestId, future);
        sendMessage(msg);
    }

    public void completeResponse(MessagePayload.RpcResponse response) {
        String requestId = response.getRequestId();
        CompletableFuture<MessagePayload.RpcResponse> future = requestFutureMap.get(requestId);
        future.complete(response);
    }

    @Override
    public void didCatchResponse(MessagePayload msg, String requestId) {
        requestFutureMap.remove(requestId);
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
        sendMessage(response);
    }

}
