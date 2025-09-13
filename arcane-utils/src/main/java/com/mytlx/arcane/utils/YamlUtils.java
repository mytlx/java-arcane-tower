package com.mytlx.arcane.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * yaml 工具类
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-07 22:33:16
 */
public class YamlUtils {

    private static Map<String, Object> yamlData;

    /*
      加载默认 application.yml
     */
    static {
        load("application.yml");
    }

    /**
     * 加载指定 yml 文件
     *
     * @param fileName yml 文件名，例如 config.yml
     */
    public static void load(String fileName) {
        try (InputStream inputStream =
                     Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
            if (Objects.isNull(inputStream)) {
                throw new RuntimeException("未找到配置文件: " + fileName);
            }
            Yaml yaml = new Yaml();
            yamlData = yaml.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("加载 yml 文件失败: " + fileName, e);
        }
    }

    /**
     * 获取配置项
     *
     * @param key 配置键，支持点语法，例如 "spring.datasource.url"
     * @return 值
     */
    public static Object get(String key) {
        if (yamlData == null) {
            throw new IllegalStateException("YML 未加载");
        }
        String[] keys = key.split("\\.");
        Object value = yamlData;
        for (String k : keys) {
            if (value instanceof Map) {
                value = ((Map<?, ?>) value).get(k);
            } else {
                return null;
            }
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key, Class<T> type) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        // 如果类型匹配直接返回
        if (type.isInstance(value)) {
            return (T) value;
        }
        // 基本类型处理
        if (type == String.class) {
            return (T) value.toString();
        } else if (type == Integer.class || type == int.class) {
            if (value instanceof Number) {
                return (T) Integer.valueOf(((Number) value).intValue());
            }
            return (T) Integer.valueOf(value.toString());
        } else if (type == Long.class || type == long.class) {
            if (value instanceof Number) {
                return (T) Long.valueOf(((Number) value).longValue());
            }
            return (T) Long.valueOf(value.toString());
        } else if (type == Boolean.class || type == boolean.class) {
            if (value instanceof Boolean) {
                return (T) value;
            }
            return (T) Boolean.valueOf(value.toString());
        }
        // 如果是 Map/List 等，直接尝试强转
        return (T) value;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> getMap(String key, Class<K> keyType, Class<V> valueType) {
        Object obj = get(key);
        if (!(obj instanceof Map<?, ?> rawMap)) return null;

        Map<K, V> result = new HashMap<>();
        for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
            K mapKey = convert(entry.getKey(), keyType);
            V mapValue = convert(entry.getValue(), valueType);
            result.put(mapKey, mapValue);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static <T> T convert(Object obj, Class<T> type) {
        if (obj == null) return null;
        if (type.isInstance(obj)) return (T) obj;
        if (type == String.class) return (T) obj.toString();
        if (type == Integer.class || type == int.class) return (T) Integer.valueOf(obj.toString());
        if (type == Long.class || type == long.class) return (T) Long.valueOf(obj.toString());
        if (type == Boolean.class || type == boolean.class) return (T) Boolean.valueOf(obj.toString());
        return (T) obj;
    }

    /**
     * 获取 String 类型的配置
     */
    public static String getString(String key) {
        Object value = get(key);
        return value == null ? null : value.toString();
    }

    /**
     * 获取 int 类型的配置
     */
    public static Integer getInt(String key) {
        Object value = get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return value == null ? null : Integer.parseInt(value.toString());
    }

    /**
     * 获取 boolean 类型的配置
     */
    public static Boolean getBoolean(String key) {
        Object value = get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return value == null ? null : Boolean.parseBoolean(value.toString());
    }

}
