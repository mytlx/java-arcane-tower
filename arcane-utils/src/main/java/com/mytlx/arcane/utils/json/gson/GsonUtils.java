package com.mytlx.arcane.utils.json.gson;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mytlx.arcane.utils.json.gson.adapter.ReflectionTypeAdapterFactory;
import com.mytlx.arcane.utils.json.gson.adapter.ThrowableAdapterFactory;
import lombok.Getter;
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
public class GsonUtils {

    /**
     * -- GETTER --
     *  获取 Gson 实例
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
     * <p>
     * 该方法的返回值可能为 {@code null}，需要在使用时进行判空处理。
     * </p>
     *
     * @param object 需要转换的对象
     * @return JSON 字符串，可能为 {@code null}
     * @throws JsonParseException gson
     */
    public static String toJsonOrThrow(Object object) throws JsonParseException {
        if (object == null) return null;
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

    // ---------------------- JsonElement ----------------------

    /**
     * 将 JSON 字符串转换为 {@link JsonElement}，该对象用于进一步的 JSON 操作。
     * <p>
     * 该方法使用 {@link Gson} 的 {@link Gson#fromJson(String, Class)} 方法将 JSON 字符串转换为 {@link JsonElement} 对象。
     * </p>
     * <p>
     * 如果 JSON 字符串为空，返回 {@code null}。
     * </p>
     *
     * @param json JSON 字符串，不能为空
     * @return {@link JsonElement} 对象，可能为 {@code null}
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
     * 格式化 JSON 字符串（美化输出）
     */
    public static String toPrettyJson(Object object) {
        return gson.toJson(object); // Gson 自带的输出是美化的
    }

    /**
     * 判断一个对象是否是有效的 JSON 格式
     */
    public static boolean isValidJson(String json) {
        try {
            gson.fromJson(json, JsonElement.class);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

    // ---------------------- List/Map ----------------------

    /**
     * 将 JSON 字符串转换为 List
     */
    public static <E> List<E> toList(String json, Class<E> clazz) {
        if (json == null || json.isEmpty()) return null;
        Type listType = TypeToken.getParameterized(List.class, clazz).getType();
        return gson.fromJson(json, listType);
    }

    /**
     * 将 JSON 字符串转换为 Map
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (json == null || json.isEmpty()) return null;
        Type mapType = TypeToken.getParameterized(Map.class, keyClass, valueClass).getType();
        return gson.fromJson(json, mapType);
    }
}