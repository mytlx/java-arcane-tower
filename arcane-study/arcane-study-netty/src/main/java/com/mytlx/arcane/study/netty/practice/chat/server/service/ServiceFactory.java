package com.mytlx.arcane.study.netty.practice.chat.server.service;

import com.mytlx.arcane.utils.YamlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 10:28:52
 */
public class ServiceFactory {

    private static final Map<Class<?>, Object> MAP = new HashMap<>();

    static {
        try {
            Map<String, String> services = YamlUtils.getMap("services", String.class, String.class);
            if (services != null) {
                for (Map.Entry<String, String> entry : services.entrySet()) {
                    MAP.put(
                            Class.forName(entry.getKey()),
                            Class.forName(entry.getValue()).getDeclaredConstructor().newInstance()
                    );
                }
            }


        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getService(Class<T> interfaceClass) {
        return (T) MAP.get(interfaceClass);
    }

}
