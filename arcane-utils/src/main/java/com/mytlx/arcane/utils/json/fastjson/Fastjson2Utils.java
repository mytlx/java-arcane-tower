package com.mytlx.arcane.utils.json.fastjson;

import com.alibaba.fastjson2.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Fastjson2 工具类
 * <p>
 * 提供 JSON 序列化和反序列化的便捷方法，基于 Fastjson2 实现。
 * 支持 Java 对象与 JSON 字符串之间的相互转换，以及常见数据结构的处理。
 * </p>
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-19 11:22:37
 */
@Slf4j
@UtilityClass
public class Fastjson2Utils {

    // ---------------------- 序列化 ----------------------

    /**
     * 将对象转换为 JSON 字符串
     * <p>
     * 该方法使用 Fastjson2 的 {@link JSON#toJSONString(Object)} 方法将对象转换为 JSON 字符串。
     * 如果转换过程中发生异常，会记录错误日志并返回 {@code null}。
     * </p>
     *
     * @param object 需要转换的对象，如果为 {@code null} 则返回 {@code null}
     * @return JSON 格式的字符串，如果转换失败则返回 {@code null}
     * @see JSON#toJSONString(Object)
     */
    public static String toJson(Object object) {
        if (object == null) return null;
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            log.error("object serialize error, {}", object, e);
            return null;
        }
    }

    /**
     * 将对象转换为 JSON 字符串（抛出异常版本）
     * <p>
     * 与 {@link #toJson(Object)} 类似，但在转换失败时会抛出异常。
     * 适用于需要明确处理错误情况的场景。
     * </p>
     *
     * @param object 需要转换的对象，如果为 {@code null} 则返回 {@code null}
     * @return JSON 格式的字符串
     * @throws IllegalArgumentException 入参为空
     * @throws JSONException            如果对象无法被序列化为 JSON
     * @see #toJson(Object)
     */
    public static String toJsonOrThrow(Object object) {
        if (object == null) throw new IllegalArgumentException("object is null");
        return JSON.toJSONString(object);
    }

    // ---------------------- 反序列化 ----------------------

    /**
     * 将 JSON 字符串转换为指定类型的对象
     * <p>
     * 示例：
     * <pre>{@code
     * // 反序列化为 User 对象
     * User user = Fastjson2Utils.fromJson(jsonString, User.class);
     * }</pre>
     *
     * @param <T>   目标类型的泛型参数
     * @param json  需要转换的 JSON 字符串，如果为 {@code null} 或空字符串则返回 {@code null}
     * @param clazz 目标类型的 Class 对象
     * @return 转换后的对象，如果解析失败或输入为 {@code null} 则返回 {@code null}
     * @see JSON#parseObject(String, Class)
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) return null;
        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            log.error("json parse error, {}", json, e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串转换为指定类型的对象（抛出异常版本）
     * <p>
     * 与 {@link #fromJson(String, Class)} 类似，但在解析失败或输入无效时会抛出异常。
     * 适用于需要明确处理错误情况的场景。
     * </p>
     *
     * @param <T>   目标类型的泛型参数
     * @param json  需要转换的 JSON 字符串，不能为 {@code null} 或空字符串
     * @param clazz 目标类型的 Class 对象，不能为 {@code null}
     * @return 转换后的对象
     * @throws IllegalArgumentException            如果 json 为 {@code null} 或空字符串，或者 clazz 为 {@code null}
     * @throws com.alibaba.fastjson2.JSONException 如果 JSON 解析失败
     * @see #fromJson(String, Class)
     */
    public static <T> T fromJsonOrThrow(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) throw new IllegalArgumentException("json can't be null or empty");
        return JSON.parseObject(json, clazz);
    }

    /**
     * 将 JSON 字符串转换为指定的泛型类型
     * <p>
     * 示例：
     * <pre>{@code
     * // 反序列化为 List<User>
     * List<User> users = Fastjson2Utils.fromJson(jsonString, new TypeReference<List<User>>() {});
     *
     * // 反序列化为 Map<String, User>
     * Map<String, User> userMap = Fastjson2Utils.fromJson(jsonString,
     *     new TypeReference<Map<String, User>>() {});
     * }</pre>
     *
     * @param <T>     目标类型的泛型参数
     * @param json    需要转换的 JSON 字符串，如果为 {@code null} 或空字符串则返回 {@code null}
     * @param typeRef 指定泛型类型的 {@link TypeReference} 对象
     * @return 转换后的对象，如果解析失败或输入为 {@code null} 则返回 {@code null}
     * @see TypeReference
     * @see JSON#parseObject(String, TypeReference, JSONReader.Feature...)
     */
    public static <T> T fromJson(String json, TypeReference<T> typeRef) {
        if (json == null || json.isEmpty()) return null;
        try {
            return JSON.parseObject(json, typeRef);
        } catch (Exception e) {
            log.error("json parse error, {}", json, e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串转换为指定的泛型类型（抛出异常版本）
     *
     * @param <T>     目标类型的泛型参数
     * @param json    需要转换的 JSON 字符串，不能为 {@code null} 或空字符串
     * @param typeRef 指定泛型类型的 {@link TypeReference} 对象，不能为 {@code null}
     * @return 转换后的对象
     * @throws IllegalArgumentException            如果 json 为 {@code null} 或空字符串，或者 typeRef 为 {@code null}
     * @throws com.alibaba.fastjson2.JSONException 如果 JSON 解析失败
     * @see #fromJson(String, TypeReference)
     */
    public static <T> T fromJsonOrThrow(String json, TypeReference<T> typeRef) {
        if (json == null || json.isEmpty()) throw new IllegalArgumentException("json can't be null or empty");
        return JSON.parseObject(json, typeRef);
    }

    /**
     * 将 JSON 字符串转换为指定类型的对象
     * <p>
     * 示例：
     * <pre>{@code
     * // 反序列化为 List<User>
     * Type type = new TypeReference<List<User>>() {}.getType();
     * List<User> users = Fastjson2Utils.fromJson(jsonString, type);
     * }</pre>
     *
     * @param <T>  目标类型的泛型参数
     * @param json 需要转换的 JSON 字符串，如果为 {@code null} 或空字符串则返回 {@code null}
     * @param type 目标类型的 {@link Type} 对象，通常通过 {@link TypeReference#getType()} 获取
     * @return 转换后的对象，如果解析失败或输入为 {@code null} 则返回 {@code null}
     * @see TypeReference
     * @see JSON#parseObject(String, Type)
     */
    public static <T> T fromJson(String json, Type type) {
        if (json == null || json.isEmpty() || type == null) return null;
        try {
            return JSON.parseObject(json, type);
        } catch (Exception e) {
            log.error("Fastjson parse(Type) error, {}", json, e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串转换为指定类型的对象（抛出异常版本）
     *
     * @param <T>  目标类型的泛型参数
     * @param json 需要转换的 JSON 字符串，不能为 {@code null} 或空字符串
     * @param type 目标类型的 {@link Type} 对象，不能为 {@code null}
     * @return 转换后的对象
     * @throws IllegalArgumentException 如果 json 为 {@code null} 或空字符串，或者 type 为 {@code null}
     * @throws JSONException            如果 JSON 解析失败
     * @see #fromJson(String, Type)
     */
    public static <T> T fromJsonOrThrow(String json, Type type) {
        if (json == null || json.isEmpty() || type == null)
            throw new IllegalArgumentException("json can't be null or empty");
        return JSON.parseObject(json, type);
    }

    // ---------------------- List/Map ----------------------

    /**
     * 将 JSON 字符串转换为指定元素类型的 List
     * <p>
     * 示例：
     * <pre>{@code
     * // 反序列化为 List<User>
     * List<User> users = Fastjson2Utils.toList(jsonString, User.class);
     * }</pre>
     *
     * @param <E>   列表元素的类型
     * @param json  需要转换的 JSON 字符串，如果为 {@code null} 或空字符串则返回 {@code null}
     * @param clazz 列表元素的 Class 对象
     * @return 包含指定类型元素的 List，如果解析失败或输入为 {@code null} 则返回 {@code null}
     * @see JSON#parseArray(String, Class)
     */
    public static <E> List<E> toList(String json, Class<E> clazz) {
        if (json == null || json.isEmpty()) return null;
        try {
            return JSON.parseArray(json, clazz);
        } catch (Exception e) {
            log.error("json parse error, {}", json, e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串转换为指定元素类型的 List，如果解析失败则抛出 JSONException
     *
     * @param <E>   列表元素的类型
     * @param json  需要转换的 JSON 字符串，不能为 {@code null} 或空字符串
     * @param clazz 列表元素的 Class 对象，不能为 {@code null}
     * @return 包含指定类型元素的 List
     * @throws IllegalArgumentException 如果 json 为 {@code null} 或空字符串，或者 clazz 为 {@code null}
     * @throws JSONException            如果 JSON 解析失败
     * @see #toList(String, Class)
     */
    public static <E> List<E> toListOrThrow(String json, Class<E> clazz) {
        if (json == null || json.isEmpty()) throw new IllegalArgumentException("json can't be null or empty");
        return JSON.parseArray(json, clazz);
    }

    /**
     * 将 JSON 字符串转换为指定键值类型的 Map
     * <p>
     * 示例：
     * <pre>{@code
     * // 反序列化为 Map<String, User>
     * Map<String, User> userMap = Fastjson2Utils.toMap(jsonString, String.class, User.class);
     * }</pre>
     *
     * @param <K>        键的类型
     * @param <V>        值的类型
     * @param json       需要转换的 JSON 字符串，如果为 {@code null} 或空字符串则返回 {@code null}
     * @param keyClass   键的 Class 对象
     * @param valueClass 值的 Class 对象
     * @return 包含指定键值类型的 Map，如果解析失败或输入为 {@code null} 则返回 {@code null}
     * @see TypeReference
     * @see JSON#parseObject(String, TypeReference, JSONReader.Feature...)
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (json == null || json.isEmpty()) return null;
        try {
            return JSON.parseObject(json, new TypeReference<>(keyClass, valueClass) {
            });
        } catch (Exception e) {
            log.error("json parse error, {}", json, e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串转换为指定键值类型的 Map，如果解析失败则抛出 JSONException
     *
     * @param <K>        键的类型
     * @param <V>        值的类型
     * @param json       需要转换的 JSON 字符串，不能为 {@code null} 或空字符串
     * @param keyClass   键的 Class 对象，不能为 {@code null}
     * @param valueClass 值的 Class 对象，不能为 {@code null}
     * @return 包含指定键值类型的 Map
     * @throws IllegalArgumentException 如果 json 为 {@code null} 或空字符串，或者 keyClass 或 valueClass 为 {@code null}
     * @throws JSONException            如果 JSON 解析失败
     * @see #toMap(String, Class, Class)
     */
    public static <K, V> Map<K, V> toMapOrThrow(String json, Class<K> keyClass, Class<V> valueClass) {
        if (json == null || json.isEmpty()) throw new IllegalArgumentException("json can't be null or empty");
        return JSON.parseObject(json, new TypeReference<>(keyClass, valueClass) {
        });
    }
    // ---------------------- 其他 ----------------------

    /**
     * 将 JSON 字符串解析为 JSONObject
     * <p>
     * 该方法将 JSON 字符串解析为 {@link JSONObject} 对象，
     * 可以方便地进行动态 JSON 操作。
     * </p>
     *
     * @param json 需要解析的 JSON 字符串，如果为 {@code null} 或空字符串则返回 {@code null}
     * @return 解析后的 JSONObject 对象，如果解析失败则返回 {@code null}
     * @see JSON#parseObject(String)
     */
    public static JSONObject parseJson(String json) {
        if (json == null || json.isEmpty()) return null;
        try {
            return JSON.parseObject(json);
        } catch (Exception e) {
            log.error("json parse error, {}", json, e);
            return null;
        }
    }

    /**
     * 将对象格式化为美化的 JSON 字符串
     * <p>
     * 该方法会将对象转换为格式化的 JSON 字符串，包含适当的缩进和换行，
     * 使输出更易读。
     * </p>
     *
     * @param object 需要格式化的对象
     * @return 格式化后的 JSON 字符串，如果输入为 {@code null} 则返回 {@code null}
     * @see JSON#toJSONString(Object, JSONWriter.Feature...)
     */
    public static String toPrettyJson(Object object) {
        if (object == null) return null;
        return JSON.toJSONString(object, JSONWriter.Feature.PrettyFormat);
    }

    /**
     * 验证字符串是否为有效的 JSON 格式
     * <p>
     * 该方法会尝试将输入字符串解析为 JSON，如果解析成功则返回 {@code true}，
     * 否则返回 {@code false}。
     * </p>
     *
     * @param json 需要验证的字符串
     * @return 如果字符串是有效的 JSON 格式则返回 {@code true}，否则返回 {@code false}
     * @see JSON#parse(String)
     */
    public static boolean isValidJson(String json) {
        if (json == null || json.isEmpty()) return false;
        try {
            JSON.parse(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

