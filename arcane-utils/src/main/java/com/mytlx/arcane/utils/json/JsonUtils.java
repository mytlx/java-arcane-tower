package com.mytlx.arcane.utils.json;

import com.mytlx.arcane.utils.json.facade.JsonFacade;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


/**
 * json 工具类，默认使用 jackson
 * <p>
 * 该类作为 JSON 工具类的简易实现，默认使用 Jackson 作为底层实现，
 * 如果想使用其他底层实现，请使用 {@link JsonFacade}
 * 提供了常用的 JSON 操作方法，包括对象与 JSON 字符串的相互转换、
 * 集合类型的转换以及 JSON 格式验证等功能。
 * </p>
 * <p>
 * 所有方法都提供了两种版本：
 * <ul>
 *   <li>静默处理版本：在出错时返回 {@code null} 或默认值，不会抛出异常</li>
 *   <li>异常抛出版本：在方法名后加 {@code OrThrow}，出错时会抛出异常</li>
 * </ul>
 * </p>
 *
 * @author TLX
 * @version 1.0.0
 * @see JsonFacade
 * @see com.mytlx.arcane.utils.json.jackson.JacksonUtils
 * @see com.mytlx.arcane.utils.json.fastjson.Fastjson2Utils
 * @see com.mytlx.arcane.utils.json.gson.GsonUtils
 * @since 2025-09-19 15:16:18
 */
public class JsonUtils {

    /**
     * 默认的 JSON 处理门面实例，使用 Jackson 作为底层实现
     */
    private static final JsonFacade JSON_FACADE = JsonFacade.def();

    /**
     * 将对象序列化为 JSON 字符串
     * <p>
     * 如果对象为 {@code null} 或序列化过程中发生异常，则返回 {@code null}。
     * 不会抛出异常，错误信息会记录到日志中。
     * </p>
     *
     * <p><b>示例：</b></p>
     * <pre>{@code
     * // 基本对象序列化
     * User user = new User("张三", 25);
     * String json = JsonUtils.toJson(user);
     * // 结果: {"name":"张三","age":25}
     *
     * // 处理 null 值
     * String nullJson = JsonUtils.toJson(null);  // 返回 null
     * }</pre>
     *
     * @param obj 需要序列化的对象，可以为 {@code null}
     * @return JSON 字符串，如果对象为 {@code null} 或序列化失败则返回 {@code null}
     * @see #toJsonOrThrow(Object)
     */
    public static String toJson(Object obj) {
        return JSON_FACADE.toJson(obj);
    }

    /**
     * 将对象序列化为 JSON 字符串（抛出异常版本）
     * <p>
     * 如果对象为 {@code null} 或序列化过程中发生异常，则抛出相应的异常。
     * 适用于需要严格处理序列化错误的场景。
     * </p>
     *
     * <p><b>示例：</b></p>
     * <pre>{@code
     * try {
     *     // 正常序列化
     *     User user = new User("张三", 25);
     *     String json = JsonUtils.toJsonOrThrow(user);
     *
     *     // 传递 null 会抛出 IllegalArgumentException
     *     // String nullJson = JsonUtils.toJsonOrThrow(null);
     * } catch (Exception e) {
     *     // 处理序列化错误
     *     logger.error("序列化对象失败", e);
     * }
     * }</pre>
     *
     * @param obj 需要序列化的对象
     * @return JSON 字符串
     * @throws IllegalArgumentException 如果对象为 {@code null}
     * @throws Exception                如果序列化过程中发生错误
     * @see #toJson(Object)
     */
    public static String toJsonOrThrow(Object obj) throws Exception {
        return JSON_FACADE.toJsonOrThrow(obj);
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的对象
     * <p>
     * 如果 JSON 字符串为 {@code null}、空或反序列化过程中发生异常，则返回 {@code null}。
     * 不会抛出异常，错误信息会记录到日志中。
     * </p>
     *
     * <p><b>示例：</b></p>
     * <pre>{@code
     * // 基本反序列化
     * String json = "{\"name\":\"张三\",\"age\":25}";
     * User user = JsonUtils.fromJson(json, User.class);
     *
     * // 处理无效 JSON
     * User invalid = JsonUtils.fromJson("invalid json", User.class);  // 返回 null
     *
     * // 处理 null 或空字符串
     * User nullUser = JsonUtils.fromJson(null, User.class);  // 返回 null
     * }</pre>
     *
     * @param <T>   目标对象类型
     * @param json  JSON 字符串，可以为 {@code null}
     * @param clazz 目标对象类型的 Class 对象
     * @return 反序列化后的对象，如果 JSON 字符串为 {@code null} 或反序列化失败则返回 {@code null}
     * @see #fromJsonOrThrow(String, Class)
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return JSON_FACADE.fromJson(json, clazz);
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的对象（抛出异常版本）
     * <p>
     * 如果 JSON 字符串为 {@code null}、空或反序列化过程中发生异常，则抛出相应的异常。
     * 适用于需要严格处理反序列化错误的场景。
     * </p>
     *
     * <p><b>示例：</b></p>
     * <pre>{@code
     * try {
     *     // 正常反序列化
     *     String json = "{\"name\":\"张三\",\"age\":25}";
     *     User user = JsonUtils.fromJsonOrThrow(json, User.class);
     *
     *     // 传递 null 或空字符串会抛出 IllegalArgumentException
     *     // User nullUser = JsonUtils.fromJsonOrThrow(null, User.class);
     *     // User emptyUser = JsonUtils.fromJsonOrThrow("", User.class);
     * } catch (Exception e) {
     *     // 处理反序列化错误
     *     logger.error("反序列化JSON失败", e);
     * }
     * }</pre>
     *
     * @param <T>   目标对象类型
     * @param json  JSON 字符串
     * @param clazz 目标对象类型的 Class 对象
     * @return 反序列化后的对象
     * @throws IllegalArgumentException 如果 JSON 字符串为 {@code null} 或空
     * @throws Exception                如果反序列化过程中发生错误
     * @see #fromJson(String, Class)
     */
    public static <T> T fromJsonOrThrow(String json, Class<T> clazz) throws Exception {
        return JSON_FACADE.fromJsonOrThrow(json, clazz);
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的对象（支持泛型）
     * <p>
     * 如果 JSON 字符串为 {@code null}、空或反序列化过程中发生异常，则返回 {@code null}。
     * 不会抛出异常，错误信息会记录到日志中。
     * </p>
     *
     * <p><b>示例：</b></p>
     * <pre>{@code
     * // 反序列化为泛型对象
     * String json = "{\"name\":\"张三\",\"age\":25}";
     * Type type = new TypeReference<Map<String, Object>>() {};
     * Map<String, Object> map = JsonUtils.fromJson(json, type);
     *
     * // 处理嵌套泛型
     * String listJson = "[\"a\", \"b\", \"c\"]";
     * Type listType = new TypeReference<List<String>>() {};
     * List<String> stringList = JsonUtils.fromJson(listJson, listType);
     * }</pre>
     *
     * @param <T>  目标对象类型
     * @param json JSON 字符串，可以为 {@code null}
     * @param type 目标类型的 Type 对象，可以使用 TypeReference 创建
     * @return 反序列化后的对象，如果 JSON 字符串为 {@code null} 或反序列化失败则返回 {@code null}
     * @see #fromJsonOrThrow(String, Type)
     * @see com.fasterxml.jackson.core.type.TypeReference
     */
    public static <T> T fromJson(String json, Type type) {
        return JSON_FACADE.fromJson(json, type);
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的对象（支持泛型，抛出异常版本）
     * <p>
     * 如果 JSON 字符串为 {@code null}、空或反序列化过程中发生异常，则抛出相应的异常。
     * 适用于需要严格处理反序列化错误的场景。
     * </p>
     *
     * <p><b>示例：</b></p>
     * <pre>{@code
     * try {
     *     // 反序列化为泛型对象
     *     String json = "{\"name\":\"张三\",\"age\":25}";
     *     Type type = new TypeReference<Map<String, Object>>() {};
     *     Map<String, Object> map = JsonUtils.fromJsonOrThrow(json, type);
     *
     *     // 处理嵌套泛型
     *     String listJson = "[\"a\", \"b\", \"c\"]";
     *     Type listType = new TypeReference<List<String>>() {};
     *     List<String> stringList = JsonUtils.fromJsonOrThrow(listJson, listType);
     * } catch (Exception e) {
     *     // 处理反序列化错误
     *     logger.error("反序列化JSON失败", e);
     * }
     * }</pre>
     *
     * @param <T>  目标对象类型
     * @param json JSON 字符串
     * @param type 目标类型的 Type 对象，可以使用 TypeReference 创建
     * @return 反序列化后的对象
     * @throws IllegalArgumentException 如果 JSON 字符串为 {@code null} 或空
     * @throws Exception                如果反序列化过程中发生错误
     * @see #fromJson(String, Type)
     * @see com.fasterxml.jackson.core.type.TypeReference
     */
    public static <T> T fromJsonOrThrow(String json, Type type) throws Exception {
        return JSON_FACADE.fromJsonOrThrow(json, type);
    }

    /**
     * 将 JSON 数组字符串反序列化为指定类型的列表
     * <p>
     * 如果 JSON 字符串为 {@code null}、空、不是有效的 JSON 数组或反序列化过程中发生异常，
     * 则返回 {@code null}。不会抛出异常，错误信息会记录到日志中。
     * </p>
     *
     * <p><b>示例：</b></p>
     * <pre>{@code
     * // 反序列化为对象列表
     * String json = "[{\"name\":\"张三\"},{\"name\":\"李四\"}]";
     * List<User> users = JsonUtils.toList(json, User.class);
     *
     * // 反序列化为字符串列表
     * String stringListJson = "[\"a\", \"b\", \"c\"]";
     * List<String> strings = JsonUtils.toList(stringListJson, String.class);
     *
     * // 处理无效输入
     * List<User> invalid = JsonUtils.toList("invalid json", User.class);  // 返回 null
     * List<User> empty = JsonUtils.toList("", User.class);               // 返回 null
     * }</pre>
     *
     * @param <E>   列表元素类型
     * @param json  JSON 数组字符串，可以为 {@code null}
     * @param clazz 列表元素类型的 Class 对象
     * @return 反序列化后的列表，如果输入无效或反序列化失败则返回 {@code null}
     * @see #toListOrThrow(String, Class)
     */
    public static <E> List<E> toList(String json, Class<E> clazz) {
        return JSON_FACADE.toList(json, clazz);
    }

    /**
     * 将 JSON 数组字符串反序列化为指定类型的列表（抛出异常版本）
     * <p>
     * 如果 JSON 字符串为 {@code null}、空、不是有效的 JSON 数组或反序列化过程中发生异常，
     * 则抛出相应的异常。适用于需要严格处理反序列化错误的场景。
     * </p>
     *
     * <p><b>示例：</b></p>
     * <pre>{@code
     * try {
     *     // 反序列化为对象列表
     *     String json = "[{\"name\":\"张三\"},{\"name\":\"李四\"}]";
     *     List<User> users = JsonUtils.toListOrThrow(json, User.class);
     *
     *     // 反序列化为字符串列表
     *     String stringListJson = "[\"a\", \"b\", \"c\"]";
     *     List<String> strings = JsonUtils.toListOrThrow(stringListJson, String.class);
     *
     *     // 传递 null 或空字符串会抛出 IllegalArgumentException
     *     // List<User> nullList = JsonUtils.toListOrThrow(null, User.class);
     *     // List<User> emptyList = JsonUtils.toListOrThrow("", User.class);
     * } catch (Exception e) {
     *     // 处理反序列化错误
     *     logger.error("反序列化列表失败", e);
     * }
     * }</pre>
     *
     * @param <E>   列表元素类型
     * @param json  JSON 数组字符串
     * @param clazz 列表元素类型的 Class 对象
     * @return 反序列化后的列表
     * @throws IllegalArgumentException 如果 JSON 字符串为 {@code null} 或空
     * @throws Exception                如果 JSON 不是有效的数组或反序列化过程中发生错误
     * @see #toList(String, Class)
     */
    public static <E> List<E> toListOrThrow(String json, Class<E> clazz) throws Exception {
        return JSON_FACADE.toListOrThrow(json, clazz);
    }

    /**
     * 将 JSON 对象字符串反序列化为指定键值类型的映射
     * <p>
     * 如果 JSON 字符串为 {@code null}、空、不是有效的 JSON 对象或反序列化过程中发生异常，
     * 则返回 {@code null}。不会抛出异常，错误信息会记录到日志中。
     * </p>
     *
     * <p><b>示例：</b></p>
     * <pre>{@code
     * // 反序列化为 Map<String, Object>
     * String json = "{\"name\":\"张三\",\"age\":25}";
     * Map<String, Object> map = JsonUtils.toMap(json, String.class, Object.class);
     *
     * // 反序列化为 Map<String, Integer>
     * String scoresJson = "{\"math\":90,\"english\":85}";
     * Map<String, Integer> scores = JsonUtils.toMap(scoresJson, String.class, Integer.class);
     *
     * // 处理无效输入
     * Map<String, Object> invalid = JsonUtils.toMap("invalid json", String.class, Object.class);  // 返回 null
     * Map<String, Object> empty = JsonUtils.toMap("", String.class, Object.class);               // 返回 null
     * }</pre>
     *
     * @param <K>        键类型
     * @param <V>        值类型
     * @param json       JSON 对象字符串，可以为 {@code null}
     * @param keyClass   键类型的 Class 对象
     * @param valueClass 值类型的 Class 对象
     * @return 反序列化后的映射，如果输入无效或反序列化失败则返回 {@code null}
     * @see #toMapOrThrow(String, Class, Class)
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
        return JSON_FACADE.toMap(json, keyClass, valueClass);
    }

    /**
     * 将 JSON 对象字符串反序列化为指定键值类型的映射（抛出异常版本）
     * <p>
     * 如果 JSON 字符串为 {@code null}、空、不是有效的 JSON 对象或反序列化过程中发生异常，
     * 则抛出相应的异常。适用于需要严格处理反序列化错误的场景。
     * </p>
     *
     * <p><b>示例：</b></p>
     * <pre>{@code
     * try {
     *     // 反序列化为 Map<String, Object>
     *     String json = "{\"name\":\"张三\",\"age\":25}";
     *     Map<String, Object> map = JsonUtils.toMapOrThrow(json, String.class, Object.class);
     *
     *     // 反序列化为 Map<String, Integer>
     *     String scoresJson = "{\"math\":90,\"english\":85}";
     *     Map<String, Integer> scores = JsonUtils.toMapOrThrow(scoresJson, String.class, Integer.class);
     *
     *     // 传递 null 或空字符串会抛出 IllegalArgumentException
     *     // Map<String, Object> nullMap = JsonUtils.toMapOrThrow(null, String.class, Object.class);
     *     // Map<String, Object> emptyMap = JsonUtils.toMapOrThrow("", String.class, Object.class);
     * } catch (Exception e) {
     *     // 处理反序列化错误
     *     logger.error("反序列化映射失败", e);
     * }
     * }</pre>
     *
     * @param <K>        键类型
     * @param <V>        值类型
     * @param json       JSON 对象字符串
     * @param keyClass   键类型的 Class 对象
     * @param valueClass 值类型的 Class 对象
     * @return 反序列化后的映射
     * @throws IllegalArgumentException 如果 JSON 字符串为 {@code null} 或空
     * @throws Exception                如果 JSON 不是有效的对象或反序列化过程中发生错误
     * @see #toMap(String, Class, Class)
     */
    public static <K, V> Map<K, V> toMapOrThrow(String json, Class<K> keyClass, Class<V> valueClass) throws Exception {
        return JSON_FACADE.toMapOrThrow(json, keyClass, valueClass);
    }

    /**
     * 将对象序列化为格式化的 JSON 字符串
     * <p>
     * 生成的 JSON 字符串会进行格式化，包含缩进和换行，便于阅读。
     * 如果对象为 {@code null} 或序列化过程中发生异常，则返回 {@code null}。
     * 不会抛出异常，错误信息会记录到日志中。
     * </p>
     *
     * <p><b>示例：</b></p>
     * <pre>{@code
     * User user = new User("张三", 25);
     * String prettyJson = JsonUtils.toPrettyJson(user);
     * // 结果:
     * // {
     * //   "name" : "张三",
     * //   "age" : 25
     * // }
     * }</pre>
     *
     * @param obj 需要序列化的对象，可以为 {@code null}
     * @return 格式化后的 JSON 字符串，如果对象为 {@code null} 或序列化失败则返回 {@code null}
     * @see #toJson(Object)
     */
    public static String toPrettyJson(Object obj) {
        return JSON_FACADE.toPrettyJson(obj);
    }

    /**
     * 验证字符串是否为有效的 JSON 格式
     * <p>
     * 检查字符串是否为有效的 JSON 格式，支持对象、数组、字符串、数字、布尔值和 null 值。
     * 如果字符串为 {@code null} 或空，则返回 {@code false}。
     * </p>
     *
     * <p><b>示例：</b></p>
     * <pre>{@code
     * // 验证有效的 JSON
     * boolean valid1 = JsonUtils.isValidJson("{\"name\":\"张三\"}");  // true
     * boolean valid2 = JsonUtils.isValidJson("[1, 2, 3]");            // true
     * boolean valid3 = JsonUtils.isValidJson("\"string\"");           // true
     * boolean valid4 = JsonUtils.isValidJson("123");                   // true
     * boolean valid5 = JsonUtils.isValidJson("true");                  // true
     * boolean valid6 = JsonUtils.isValidJson("null");                  // true
     *
     * // 验证无效的 JSON
     * boolean invalid1 = JsonUtils.isValidJson(null);                  // false
     * boolean invalid2 = JsonUtils.isValidJson("");                    // false
     * boolean invalid3 = JsonUtils.isValidJson("{name: 张三}");        // false (键需要引号)
     * boolean invalid4 = JsonUtils.isValidJson("{'name': '张三'}");    // false (应使用双引号)
     * }</pre>
     *
     * @param json 要验证的字符串，可以为 {@code null}
     * @return 如果字符串是有效的 JSON 格式则返回 {@code true}，否则返回 {@code false}
     */
    public static boolean isValidJson(String json) {
        return JSON_FACADE.isValidJson(json);
    }
}
