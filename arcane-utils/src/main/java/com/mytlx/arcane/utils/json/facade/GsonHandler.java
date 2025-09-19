package com.mytlx.arcane.utils.json.facade;

import com.google.gson.JsonParseException;
import com.mytlx.arcane.utils.json.gson.GsonUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Gson 实现的 JSON 处理类
 * <p>
 * 该类是 {@link JsonHandler} 接口的 Gson 实现，提供了基于 Gson 库的 JSON 序列化和反序列化功能。
 * 通过实现 {@link JsonHandler} 接口，该类可以无缝集成到使用门面模式的 JSON 处理流程中。
 * </p>
 *
 * <p><b>使用示例：</b></p>
 * <pre>{@code
 * // 创建 Gson 处理器
 * JsonHandler jsonHandler = new GsonHandler();
 *
 * // 序列化对象为 JSON 字符串
 * String json = jsonHandler.toJson(user);
 *
 * // 反序列化 JSON 字符串为对象
 * User user = jsonHandler.fromJson(json, User.class);
 *
 * // 处理集合类型
 * List<User> users = jsonHandler.toList(jsonArray, User.class);
 * Map<String, User> userMap = jsonHandler.toMap(jsonObject, String.class, User.class);
 * }</pre>
 *
 * @author TLX
 * @version 1.0.0
 * @see JsonHandler
 * @see JsonFacade
 * @see GsonUtils
 * @since 2025-09-19 11:51:52
 */
public class GsonHandler implements JsonHandler {

    /**
     * 将对象序列化为 JSON 字符串
     * <p>
     * 如果对象为 {@code null} 或序列化过程中发生异常，则返回 {@code null}。
     * 不会抛出异常，错误信息会记录到日志中。
     * </p>
     *
     * @param object 需要序列化的对象，可以为 {@code null}
     * @return JSON 字符串，如果对象为 {@code null} 或序列化失败则返回 {@code null}
     * @see #toJsonOrThrow(Object)
     */
    @Override
    public String toJson(Object object) {
        return GsonUtils.toJson(object);
    }

    /**
     * 将对象序列化为 JSON 字符串
     * <p>
     * 如果对象为 {@code null} 或序列化过程中发生异常，则抛出相应的异常。
     * </p>
     *
     * @param object 需要序列化的对象
     * @return JSON 字符串
     * @throws IllegalArgumentException 如果对象为 {@code null}
     * @throws JsonParseException       如果序列化过程中发生错误
     * @see #toJson(Object)
     */
    @Override
    public String toJsonOrThrow(Object object) throws JsonParseException {
        return GsonUtils.toJsonOrThrow(object);
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的对象
     * <p>
     * 如果 JSON 字符串为 {@code null} 或反序列化过程中发生异常，则返回 {@code null}。
     * 不会抛出异常，错误信息会记录到日志中。
     * </p>
     *
     * @param <T>   目标对象类型
     * @param json  JSON 字符串，可以为 {@code null}
     * @param clazz 目标对象类型的 Class 对象
     * @return 反序列化后的对象，如果 JSON 字符串为 {@code null} 或反序列化失败则返回 {@code null}
     * @see #fromJsonOrThrow(String, Class)
     */
    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        return GsonUtils.fromJson(json, clazz);
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的对象
     * <p>
     * 如果 JSON 字符串为 {@code null} 或反序列化过程中发生异常，则抛出相应的异常。
     * </p>
     *
     * @param <T>   目标对象类型
     * @param json  JSON 字符串
     * @param clazz 目标对象类型的 Class 对象
     * @return 反序列化后的对象
     * @throws IllegalArgumentException 如果 JSON 字符串为 {@code null}
     * @throws JsonParseException       如果反序列化过程中发生错误
     * @see #fromJson(String, Class)
     */
    @Override
    public <T> T fromJsonOrThrow(String json, Class<T> clazz) throws JsonParseException {
        return GsonUtils.fromJsonOrThrow(json, clazz);
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的对象
     * <p>
     * 如果 JSON 字符串为 {@code null} 或反序列化过程中发生异常，则返回 {@code null}。
     * 不会抛出异常，错误信息会记录到日志中。
     * </p>
     *
     * @param <T>  目标对象类型
     * @param json JSON 字符串，可以为 {@code null}
     * @param type 目标对象类型的 Type 对象
     * @return 反序列化后的对象，如果 JSON 字符串为 {@code null} 或反序列化失败则返回 {@code null}
     * @see #fromJsonOrThrow(String, Type)
     */
    @Override
    public <T> T fromJson(String json, Type type) {
        return GsonUtils.fromJson(json, type);
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的对象
     * <p>
     * 如果 JSON 字符串为 {@code null} 或反序列化过程中发生异常，则抛出相应的异常。
     * </p>
     *
     * @param <T>  目标对象类型
     * @param json JSON 字符串
     * @param type 目标对象类型的 Type 对象
     * @return 反序列化后的对象
     * @throws IllegalArgumentException 如果 JSON 字符串为 {@code null}
     * @throws JsonParseException       如果反序列化过程中发生错误
     * @see #fromJson(String, Type)
     */
    @Override
    public <T> T fromJsonOrThrow(String json, Type type) throws JsonParseException {
        return GsonUtils.fromJsonOrThrow(json, type);
    }

    /**
     * 将 JSON 数组字符串反序列化为指定元素类型的列表
     * <p>
     * 如果 JSON 字符串为 {@code null}、空或反序列化过程中发生异常，则返回 {@code null}。
     * 不会抛出异常，错误信息会记录到日志中。
     * </p>
     *
     * @param <E>   列表元素类型
     * @param json  JSON 数组字符串，例如 {@code [{"name":"Alice"},{"name":"Bob"}]}
     * @param clazz 列表元素类型的 Class 对象
     * @return 包含反序列化后对象的列表，如果 JSON 为 {@code null}、空或反序列化失败则返回 {@code null}
     * @see #toListOrThrow(String, Class)
     */
    @Override
    public <E> List<E> toList(String json, Class<E> clazz) {
        return GsonUtils.toList(json, clazz);
    }

    /**
     * 将 JSON 数组字符串反序列化为指定元素类型的列表
     * <p>
     * 如果 JSON 字符串为 {@code null}、空或反序列化过程中发生异常，则抛出相应的异常。
     * </p>
     *
     * @param <E>   列表元素类型
     * @param json  JSON 数组字符串，例如 {@code [{"name":"Alice"},{"name":"Bob"}]}
     * @param clazz 列表元素类型的 Class 对象
     * @return 包含反序列化后对象的列表
     * @throws IllegalArgumentException 如果 JSON 字符串为 {@code null} 或空，或者 clazz 为 {@code null}
     * @throws JsonParseException       如果反序列化过程中发生错误
     * @see #toList(String, Class)
     */
    @Override
    public <E> List<E> toListOrThrow(String json, Class<E> clazz) throws JsonParseException {
        return GsonUtils.toListOrThrow(json, clazz);
    }

    /**
     * 将 JSON 对象字符串反序列化为指定键值类型的映射
     * <p>
     * 如果 JSON 字符串为 {@code null}、空或反序列化过程中发生异常，则返回 {@code null}。
     * 不会抛出异常，错误信息会记录到日志中。
     * </p>
     *
     * @param <K>        映射键类型
     * @param <V>        映射值类型
     * @param json       JSON 对象字符串，例如 {"1":{"name":"Alice"},"2":{"name":"Bob"}}
     * @param keyClass   键类型的 Class 对象
     * @param valueClass 值类型的 Class 对象
     * @return 包含反序列化后键值对的映射，如果 JSON 为 {@code null}、空或反序列化失败则返回 {@code null}
     * @see #toMapOrThrow(String, Class, Class)
     */
    @Override
    public <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
        return GsonUtils.toMap(json, keyClass, valueClass);
    }

    /**
     * 将 JSON 对象字符串反序列化为指定键值类型的映射
     * <p>
     * 如果 JSON 字符串为 {@code null}、空或反序列化过程中发生异常，则抛出相应的异常。
     * </p>
     *
     * @param <K>        映射键类型
     * @param <V>        映射值类型
     * @param json       JSON 对象字符串，例如 {"1":{"name":"Alice"},"2":{"name":"Bob"}}
     * @param keyClass   键类型的 Class 对象
     * @param valueClass 值类型的 Class 对象
     * @return 包含反序列化后键值对的映射
     * @throws IllegalArgumentException 如果 JSON 字符串为 {@code null} 或空，或者 keyClass/valueClass 为 {@code null}
     * @throws JsonParseException       如果反序列化过程中发生错误
     * @see #toMap(String, Class, Class)
     */
    @Override
    public <K, V> Map<K, V> toMapOrThrow(String json, Class<K> keyClass, Class<V> valueClass) throws JsonParseException {
        return GsonUtils.toMapOrThrow(json, keyClass, valueClass);
    }

    /**
     * 将对象格式化为易读的 JSON 字符串
     * <p>
     * 生成的 JSON 字符串会进行格式化，包含缩进和换行，适合阅读和日志输出。
     * 如果对象为 {@code null} 或格式化过程中发生异常，则返回 {@code null}。
     * 不会抛出异常，错误信息会记录到日志中。
     * </p>
     *
     * @param object 需要格式化的对象，可以为 {@code null}
     * @return 格式化后的 JSON 字符串，如果对象为 {@code null} 或格式化失败则返回 {@code null}
     */
    @Override
    public String toPrettyJson(Object object) {
        return GsonUtils.toPrettyJson(object);
    }

    /**
     * 验证字符串是否为有效的 JSON 格式
     * <p>
     * 该方法会检查字符串是否可以成功解析为 JSON 对象或数组。
     * 如果字符串为 {@code null} 或空，则返回 {@code false}。
     * 不会抛出异常，错误信息会记录到日志中。
     * </p>
     *
     * @param json 需要验证的字符串，可以为 {@code null}
     * @return 如果字符串是有效的 JSON 格式则返回 {@code true}，否则返回 {@code false}
     */
    @Override
    public boolean isValidJson(String json) {
        return GsonUtils.isValidJson(json);
    }
}
