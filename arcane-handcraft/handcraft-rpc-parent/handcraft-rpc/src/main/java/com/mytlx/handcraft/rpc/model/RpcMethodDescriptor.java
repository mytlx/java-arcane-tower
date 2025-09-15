package com.mytlx.handcraft.rpc.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.util.Arrays;
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
        RpcMethodDescriptor md = new RpcMethodDescriptor()
                .setMethodId(generateMethodId(method))
                .setClassName(method.getDeclaringClass().getName())
                .setMethodName(method.getName())
                .setParameterCount(method.getParameterCount())
                .setParameterTypes(Arrays.stream(method.getParameterTypes()).map(Class::getName).toList())
                .setHasReturnValue(false)
                .setReturnType(null);

        if (!method.getReturnType().equals(Void.TYPE)) {
            md.setHasReturnValue(true);
            md.setReturnType(method.getReturnType().getName());
        }

        return md;
    }

    public static String generateMethodId(Method method) {
        String[] parameterTypes = Arrays.stream(method.getParameterTypes()).map(Class::getName).toArray(String[]::new);
        return generateMethodId(method.getName(), parameterTypes, method.getReturnType().getName());
    }

    public static String generateMethodId(String methodName, String[] parameterTypeNames, String returnTypeName) {
        String methodId = String.join(".", methodName, String.valueOf(parameterTypeNames.length));

        for (String type : parameterTypeNames) {
            methodId = String.join(".", methodId, type);
        }

        if (returnTypeName != null) {
            methodId = String.join(".", methodId, returnTypeName);
        }

        return methodId;
    }

}
