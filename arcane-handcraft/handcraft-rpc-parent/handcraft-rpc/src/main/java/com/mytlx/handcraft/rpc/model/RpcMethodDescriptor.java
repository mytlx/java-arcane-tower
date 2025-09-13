package com.mytlx.handcraft.rpc.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 21:29:55
 */
@Data
@Accessors(chain = true)
public class RpcMethodDescriptor {
    private String methodId;
    private String className;
    private String methodName;
    private Integer parameterCount;
    private List<String> parameterTypes;
    private boolean hasReturnValue;
    private String returnType;

    public static RpcMethodDescriptor build(Method method) {
        List<String> parameterTypes = new ArrayList<>();
        RpcMethodDescriptor md = new RpcMethodDescriptor()
                .setMethodId(null)
                .setClassName(method.getDeclaringClass().getName())
                .setMethodName(method.getName())
                .setParameterCount(method.getParameterCount())
                .setParameterTypes(parameterTypes)
                .setHasReturnValue(false)
                .setReturnType(null);

        String methodId = String.join(".", method.getName(), String.valueOf(method.getParameterCount()));

        for (Class<?> type : method.getParameterTypes()) {
            methodId = String.join(".", methodId, type.getSimpleName());
            parameterTypes.add(type.getName());
        }

        if (!method.getReturnType().equals(Void.TYPE)) {
            md.setHasReturnValue(true);
            md.setReturnType(method.getReturnType().getName());
            methodId = String.join(".", methodId, method.getReturnType().getSimpleName());
        }
        md.setMethodId(methodId);

        return md;
    }

}
