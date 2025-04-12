package com.mytlx.dev.solutions.drilldown.router;

import com.mytlx.arcane.utils.date.LocalDateTimeUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-12 14:17
 */
public class In {

    private final Map<String, Object> params = new HashMap<>();

    /**
     * 模拟获取登录用户userId
     */
    public Long getLoginUserId() {
        return LocalDateTimeUtils.getCurrentTimestampMillis();
    }

    /**
     * 模拟获取平台用户id
     */
    public Long getPlatformUserId() {
        return LocalDateTimeUtils.getCurrentTimestampMillis();
    }

    public Long getLong(String name) {
        return getLong(name, null);
    }

    public Long getLong(String name, Long defaultValue) {
        return get(Long.class, name, defaultValue);
    }

    public Integer getInteger(String name) {
        return getInteger(name, null);
    }

    public Integer getInteger(String name, Integer defaultValue) {
        return get(Integer.class, name, defaultValue);
    }

    public String getString(String name) {
        return getString(name, null);
    }

    public String getString(String name, String defaultValue) {
        return get(String.class, name, defaultValue);
    }

    public <T> T get(Class<T> clazz, String name, T defaultValue) {
        Object o = params.get(name);
        if (o == null) {
            return defaultValue;
        }
        if (clazz.isInstance(o)) {
            return clazz.cast(o);
        }
        return defaultValue;
    }

    public In setParam(String name, Object value) {
        params.put(name, value);
        return this;
    }

}
