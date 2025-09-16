package com.mytlx.handcraft.rpc.config;

import com.mytlx.handcraft.rpc.client.RpcClient;
import com.mytlx.handcraft.rpc.model.MessagePayload;
import com.mytlx.handcraft.rpc.model.MessageTypeEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-16 11:02:52
 */
@Slf4j
@Data
public class RemoteServiceFactoryBean<T> implements FactoryBean<T> {

    private String targetClientId;

    private Class<T> rpcInterfaceClass;

    private RpcClient rpcClient;

    // public RemoteServiceFactoryBean(Class<T> rpcInterfaceClass) {
    //     this.rpcInterfaceClass = rpcInterfaceClass;
    // }

    @SuppressWarnings("unchecked")
    @Override
    public T getObject() throws Exception {
        return (T) Proxy.newProxyInstance(
                rpcInterfaceClass.getClassLoader(),
                new Class[]{rpcInterfaceClass},
                (proxy, method, args) -> {
                    String requestId = UUID.randomUUID().toString();
                    MessagePayload msg = new MessagePayload()
                            .setClientId(rpcClient.getClientId())
                            .setMessageType(MessageTypeEnum.CALL)
                            .setPayload(
                                    new MessagePayload.RpcRequest()
                                            .setRequestClientId(targetClientId)
                                            .setRequestId(requestId)
                                            .setRequestMethodSimpleName(method.getName())
                                            .setRequestClassName(method.getDeclaringClass().getName())
                                            .setReturnValueType(method.getReturnType().getName())
                                            .setParameterTypes(Arrays.stream(method.getParameterTypes()).map(Class::getName).toArray(String[]::new))
                                            .setParameters(args)
                            );

                    CompletableFuture<MessagePayload.RpcResponse> future = new CompletableFuture<>();
                    rpcClient.sendRequest(msg, requestId, future);
                    try {
                        MessagePayload.RpcResponse response = future.get(10, TimeUnit.SECONDS);
                        return response.getReturnValue();
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        return "超时";
                    }
                }
        );
    }

    @Override
    public Class<?> getObjectType() {
        return rpcInterfaceClass;
    }
}
