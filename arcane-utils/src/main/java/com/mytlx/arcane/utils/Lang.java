package com.mytlx.arcane.utils;

import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-12 18:12
 */
public class Lang {

    @SuppressWarnings("unchecked")
    public static <T> T get(Object obj, String field) throws Exception {
        return (T) FieldUtils.traverseField(obj.getClass(), field).get(obj);
    }

    public static void set(Object obj, String field, Object target) throws Exception {
        FieldUtils.traverseField(obj.getClass(), field).set(obj, target);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T> List<T> collect(Collection<?> list, String collectProp) {
        if (list == null) return null;
        if (list.isEmpty()) return new ArrayList<>(0);
        if (collectProp == null || collectProp.isEmpty()) {
            throw new IllegalArgumentException("属性名 collectProp 不能为空");
        }

        Method getter = null;
        Map<Class<?>, Method> methodCache = new HashMap<>();
        List<T> result = new ArrayList<>(list.size());

        for (Object obj : list) {
            if (obj == null) continue;

            Class<?> clazz = obj.getClass();
            getter = methodCache.get(clazz);
            if (getter == null) {
                getter = MethodUtils.getGetter(clazz, collectProp);
                if (getter == null) {
                    throw new RuntimeException("类[" + clazz.getSimpleName() + "]缺少[" + collectProp + "]属性");
                }
                methodCache.put(clazz, getter);
            }

            Object value = getter.invoke(obj);
            result.add((T) value);
        }
        return result;
    }

}
