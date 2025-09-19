package com.mytlx.arcane.utils.json.gson;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mytlx.arcane.utils.json.gson.adapter.ReflectionTypeAdapterFactory;
import com.mytlx.arcane.utils.json.gson.adapter.ThrowableAdapterFactory;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Gson 工具类
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-15 14:39
 */
@Slf4j
@UtilityClass
public class GsonUtils {

    /**
     * -- GETTER --
     * 获取 Gson 实例
     */
    @Getter
    private static final Gson gson;

    static {
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls()
                .disableHtmlEscaping()
                .registerTypeAdapterFactory(new ThrowableAdapterFactory())
                .registerTypeAdapterFactory(new ReflectionTypeAdapterFactory())
                // .registerTypeHierarchyAdapter(Throwable.class, new TypeAdapter<Throwable>() {
                //     @Override
                //     public void write(JsonWriter out, Throwable t) throws IOException {
                //         if (t == null) {
                //             out.nullValue();
                //             return;
                //         }
                //         out.beginObject();
                //         out.name("class").value(t.getClass().getName());
                //         out.name("message").value(t.getMessage());
                //         // stackTrace
                //         out.name("stackTrace").beginArray();
                //         for (StackTraceElement e : t.getStackTrace()) {
                //             out.value(e.toString());
                //         }
                //         out.endArray();
                //         // cause
                //         if (t.getCause() != null && t.getCause() != t) {
                //             out.name("cause");
                //             write(out, t.getCause());
                //         }
                //         out.endObject();
                //     }
                //
                //     @Override
                //     public Throwable read(JsonReader in) throws IOException {
                //         // 可以只反序列化 message 或返回 null
                //         return null;
                //     }
                // })
                .create();
    }

    // ---------------------- 序列化 ----------------------

    /**
     * 将对象转换为 JSON 字符串
     * <p>
     * 该方法使用 {@link Gson} 的 {@link Gson#toJson(Object)} 方法将对象转换为 JSON 字符串。
     * </p>
     * <p>
     * 该方法的返回值可能为 {@code null}，需要在使用时进行判空处理。
     * </p>
     *
     * @param object 需要转换的对象
     * @return JSON 字符串，可能为 {@code null}
     */
    public static String toJson(Object object) throws JsonParseException {
        if (object == null) return null;
        try {
            return gson.toJson(object);
        } catch (JsonParseException e) {
            log.error("object serialize error, {}", object, e);
            return null;
        }
    }

    /**
     * 将对象转换为 JSON 字符串
     * <p>
     * 该方法使用 {@link Gson} 的 {@link Gson#toJson(Object)} 方法将对象转换为 JSON 字符串。
     * </p>
     *
     * @param object 需要转换的对象
     * @return JSON 字符串，可能为 {@code null}
     * @throws IllegalArgumentException 入参为空
     * @throws JsonParseException       gson
     */
    public static String toJsonOrThrow(Object object) throws JsonParseException {
        if (object == null) throw new IllegalArgumentException("object is null");
        return gson.toJson(object);
    }

    // ---------------------- 反序列化 ----------------------

    /**
     * 将 JSON 字符串转换为指定类型的对象
     * <p>
     * 该方法使用 {@link Gson} 的 {@link Gson#fromJson(String, Class)} 方法将 JSON 字符串转换为指定类型的对象。
     * </p>
     * <p>
     * 该方法的返回值可能为 {@code null}，需要在使用时进行判空处理。
     * </p>
     *
     * @param json  JSON 字符串，不能为空
     * @param clazz 指定类型的 Class 对象，不能为空
     * @return 指定类型的对象，可能为 {@code null}
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) return null;
        try {
            return gson.fromJson(json, clazz);
        } catch (JsonParseException e) {
            log.error("json parse error, {}", json, e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串转换为指定类型的对象
     * <p>
     * 该方法使用 {@link Gson} 的 {@link Gson#fromJson(String, Class)} 方法将 JSON 字符串转换为指定类型的对象。
     * </p>
     * <p>
     * 该方法的返回值可能为 {@code null}，需要在使用时进行判空处理。
     * </p>
     *
     * @param json  JSON 字符串，不能为空
     * @param clazz 指定类型的 Class 对象，不能为空
     * @return 指定类型的对象，可能为 {@code null}
     * @throws JsonParseException gson
     */
    public static <T> T fromJsonOrThrow(String json, Class<T> clazz) throws JsonParseException {
        if (json == null || json.isEmpty()) throw new IllegalArgumentException("json can't be null or empty");
        return gson.fromJson(json, clazz);
    }

    /**
     * 将 JSON 字符串转换为指定的泛型类型
     * <p>
     * 该方法使用 {@link Gson} 的 {@link Gson#fromJson(String, Type)} 方法将 JSON 字符串转换为指定的泛型类型。
     * </p>
     * <p>
     * 该方法的返回值可能为 {@code null}，需要在使用时进行判空处理。
     * </p>
     *
     * @param json      JSON 字符串，不能为空
     * @param typeToken 指定泛型类型的 {@link TypeToken} 对象，不能为空
     * @return 指定泛型类型的对象，可能为 {@code null}
     */
    public static <T> T fromJson(String json, TypeToken<T> typeToken) {
        if (json == null || json.isEmpty()) return null;
        try {
            // 使用 Gson 解析 JSON 字符串为指定类型的对象
            return gson.fromJson(json, typeToken.getType());
        } catch (JsonParseException e) {
            log.error("json parse error, {}", json, e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串转换为指定的泛型类型
     * <p>
     * 该方法使用 {@link Gson} 的 {@link Gson#fromJson(String, Type)} 方法将 JSON 字符串转换为指定的泛型类型。
     * </p>
     * <p>
     * 该方法的返回值可能为 {@code null}，需要在使用时进行判空处理。
     * </p>
     *
     * @param json      JSON 字符串，不能为空
     * @param typeToken 指定泛型类型的 {@link TypeToken} 对象，不能为空
     * @return 指定泛型类型的对象，可能为 {@code null}
     * @throws JsonParseException gson
     */
    public static <T> T fromJsonOrThrow(String json, TypeToken<T> typeToken) throws JsonParseException {
        if (json == null || json.isEmpty()) throw new IllegalArgumentException("json can't be null or empty");
        return gson.fromJson(json, typeToken.getType());
    }

    /**
     * 将 JSON 字符串转换为指定类型的对象
     * <p>
     * 该方法使用 {@link Gson} 的 {@link Gson#fromJson(String, Type)} 方法将 JSON 字符串转换为指定类型的对象。
     * 支持复杂泛型类型的反序列化。
     * </p>
     * <p>
     * 示例：
     * <pre>{@code
     * // 反序列化为 List<User>
     * Type type = new TypeToken<List<User>>(){}.getType();
     * List<User> users = GsonUtils.fromJson(jsonString, type);
     *
     * // 反序列化为 Map<String, User>
     * Type mapType = new TypeToken<Map<String, User>>(){}.getType();
     * Map<String, User> userMap = GsonUtils.fromJson(jsonString, mapType);
     * }</pre>
     *
     * @param <T>  目标类型的泛型参数
     * @param json 需要转换的 JSON 字符串，如果为 {@code null} 或空字符串则返回 {@code null}
     * @param type 目标类型的 {@link Type} 对象，通常通过 {@link TypeToken#getType()} 获取，不能为 {@code null}
     * @return 转换后的对象，如果解析失败或输入为 {@code null} 则返回 {@code null}
     * @see TypeToken
     * @see Gson#fromJson(String, Type)
     */
    public static <T> T fromJson(String json, Type type) {
        if (json == null || json.isEmpty() || type == null) return null;
        try {
            return gson.fromJson(json, type);
        } catch (Exception e) {
            log.error("Gson parse(Type) error, {}", json, e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串转换为指定类型的对象（抛出异常版本）
     * <p>
     * 与 {@link #fromJson(String, Type)} 类似，但在解析失败或输入无效时会抛出异常。
     * 适用于需要明确处理错误情况的场景。
     * </p>
     *
     * @param <T>  目标类型的泛型参数
     * @param json 需要转换的 JSON 字符串，不能为 {@code null} 或空字符串
     * @param type 目标类型的 {@link Type} 对象，不能为 {@code null}
     * @return 转换后的对象
     * @throws IllegalArgumentException 如果 json 为 {@code null} 或空字符串，或者 type 为 {@code null}
     * @throws JsonParseException       如果 JSON 解析失败
     * @see #fromJson(String, Type)
     * @see Gson#fromJson(String, Type)
     */
    public static <T> T fromJsonOrThrow(String json, Type type) {
        if (json == null || json.isEmpty() || type == null) {
            throw new IllegalArgumentException("json and type parameters cannot be null or empty");
        }
        return gson.fromJson(json, type);
    }

    // ---------------------- JsonElement ----------------------

    /**
     * 将 JSON 字符串转换为 {@link JsonElement}，该对象用于进一步的 JSON 操作。
     * <p>
     * 该方法使用 {@link Gson} 的 {@link Gson#fromJson(String, Class)} 方法将 JSON 字符串转换为 {@link JsonElement} 对象。
     * 如果解析过程中发生错误，会记录错误日志并返回 {@code null}。
     * </p>
     *
     * @param json 需要解析的 JSON 字符串，如果为 {@code null} 或空字符串则返回 {@code null}
     * @return 解析后的 {@link JsonElement} 对象，如果解析失败或输入为 {@code null} 则返回 {@code null}
     * @see JsonElement
     * @see Gson#fromJson(String, Class)
     */
    public static JsonElement parseJson(String json) {
        if (json == null || json.isEmpty()) return null;
        try {
            return gson.fromJson(json, JsonElement.class);
        } catch (JsonSyntaxException e) {
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
     * @see Gson#toJson(Object)
     */
    public static String toPrettyJson(Object object) {
        if (object == null) return null;
        return gson.toJson(object); // Gson 自带的输出是美化的
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
     * @see JsonElement
     * @see Gson#fromJson(String, Class)
     */
    public static boolean isValidJson(String json) {
        if (json == null || json.isBlank()) return false;
        try {
            gson.fromJson(json, JsonElement.class);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

    // ---------------------- List/Map ----------------------

    /**
     * 将 JSON 字符串转换为指定元素类型的 List
     * <p>
     * 示例：
     * <pre>{@code
     * List<User> users = GsonUtils.toList(jsonString, User.class);
     * }</pre>
     *
     * @param <E>   列表元素的类型
     * @param json  需要转换的 JSON 字符串，如果为 {@code null} 或空字符串则返回 {@code null}
     * @param clazz 列表元素的 Class 对象
     * @return 包含指定类型元素的 List，如果解析失败或输入为 {@code null} 则返回 {@code null}
     * @see TypeToken
     * @see Gson#fromJson(String, Type)
     */
    public static <E> List<E> toList(String json, Class<E> clazz) {
        if (json == null || json.isEmpty()) return null;
        try {
            Type listType = TypeToken.getParameterized(List.class, clazz).getType();
            return gson.fromJson(json, listType);
        } catch (JsonSyntaxException e) {
            log.error("json parse error, {}", json, e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串转换为指定元素类型的 List，如果解析失败则抛出 JsonSyntaxException
     * <p>
     * 示例：
     * <pre>{@code
     * List<User> users = GsonUtils.toListOrThrow(jsonString, User.class);
     * }</pre>
     *
     * @param <E>   列表元素的类型
     * @param json  需要转换的 JSON 字符串，不允许为 {@code null} 或空字符串
     * @param clazz 列表元素的 Class 对象
     * @return 包含指定类型元素的 List
     * @throws JsonSyntaxException 如果 JSON 字符串解析失败
     * @see TypeToken
     * @see Gson#fromJson(String, Type)
     * @see GsonUtils#toList(String, Class)
     */
    public static <E> List<E> toListOrThrow(String json, Class<E> clazz) throws JsonSyntaxException {
        if (json == null || json.isEmpty()) throw new IllegalArgumentException("json can't be null or empty");
        Type listType = TypeToken.getParameterized(List.class, clazz).getType();
        return gson.fromJson(json, listType);
    }

    /**
     * 将 JSON 字符串转换为指定键值类型的 Map
     * <p>
     * 该方法使用 {@link Gson} 的 {@link Gson#fromJson(String, Type)} 方法将 JSON 字符串转换为指定键值类型的 Map。
     * 支持复杂泛型类型的反序列化。
     * </p>
     * <p>
     * 示例：
     * <pre>{@code
     * // 反序列化为 Map<String, Integer>
     * Map<String, Integer> map = GsonUtils.toMap(jsonString, String.class, Integer.class);
     * }</pre>
     *
     * @param <K>        键的类型
     * @param <V>        值的类型
     * @param json       需要转换的 JSON 字符串，如果为 {@code null} 或空字符串则返回 {@code null}
     * @param keyClass   键的 Class 对象
     * @param valueClass 值的 Class 对象
     * @return 包含指定键值类型的 Map，如果解析失败或输入为 {@code null} 则返回 {@code null}
     * @see TypeToken
     * @see Gson#fromJson(String, Type)
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (json == null || json.isEmpty()) return null;
        try {
            Type mapType = TypeToken.getParameterized(Map.class, keyClass, valueClass).getType();
            return gson.fromJson(json, mapType);
        } catch (JsonSyntaxException e) {
            log.error("json parse error, {}", json, e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串转换为指定键值类型的 Map，如果解析失败则抛出 JsonSyntaxException
     * <p>
     * 示例：
     * <pre>{@code
     * Map<String, Integer> map = GsonUtils.toMapOrThrow(jsonString, String.class, Integer.class);
     * }</pre>
     *
     * @param <K>        键的类型
     * @param <V>        值的类型
     * @param json       需要转换的 JSON 字符串，不允许为 {@code null} 或空字符串
     * @param keyClass   键的 Class 对象
     * @param valueClass 值的 Class 对象
     * @return 包含指定键值类型的 Map
     * @throws JsonSyntaxException 如果 JSON 字符串解析失败
     * @see TypeToken
     * @see Gson#fromJson(String, Type)
     * @see GsonUtils#toMap(String, Class, Class)
     */
    public static <K, V> Map<K, V> toMapOrThrow(String json, Class<K> keyClass, Class<V> valueClass) throws JsonSyntaxException {
        if (json == null || json.isEmpty()) throw new IllegalArgumentException("json can't be null or empty");
        Type mapType = TypeToken.getParameterized(Map.class, keyClass, valueClass).getType();
        return gson.fromJson(json, mapType);
    }
}