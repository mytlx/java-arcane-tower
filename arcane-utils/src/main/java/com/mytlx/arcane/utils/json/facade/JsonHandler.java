package com.mytlx.arcane.utils.json.facade;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * 门面模式：JSON 处理接口
 * <p>
 * 定义了一组用于 JSON 序列化和反序列化的通用方法，作为不同 JSON 库的抽象层。
 * 通过实现此接口，可以方便地切换底层 JSON 库（如 Gson、Jackson、Fastjson 等）。
 * </p>
 * <p>
 * 特性：
 * <ul>
 *   <li>支持基本对象与 JSON 字符串的相互转换</li>
 *   <li>支持泛型类型的序列化和反序列化</li>
 *   <li>提供安全（返回 null）和抛出异常两种风格的 API</li>
 *   <li>支持 List、Map 等集合类型的转换</li>
 *   <li>支持 JSON 格式验证</li>
 *   <li>支持美化输出（格式化）的 JSON 字符串</li>
 * </ul>
 * </p>
 * <p>
 * 示例实现：
 * <pre>{@code
 * // 使用 Gson 实现
 * JsonHandler jsonHandler = new GsonHandler();
 * 
 * // 使用 Jackson 实现
 * // JsonHandler jsonHandler = new JacksonHandler();
 * 
 * // 使用 Fastjson 实现
 * // JsonHandler jsonHandler = new Fastjson2Handler();
 * }</pre>
 * </p>
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-19 11:30:24
 * @see com.mytlx.arcane.utils.json.facade.GsonHandler
 * @see com.mytlx.arcane.utils.json.facade.JacksonHandler
 * @see com.mytlx.arcane.utils.json.facade.Fastjson2Handler
 * @see JsonFacade
 */
public interface JsonHandler {

    /**
     * 将对象序列化为 JSON 字符串
     * <p>
     * 如果对象为 null 或序列化过程中发生异常，则返回 null。
     * </p>
     * <p>
     * 示例：
     * <pre>{@code
     * User user = new User("John", 30);
     * String json = jsonHandler.toJson(user);
     * // 结果: {"name":"John","age":30}
     * }</pre>
     * </p>
     *
     * @param object 需要序列化的对象，可以为 null
     * @return JSON 字符串，如果对象为 null 或序列化失败则返回 null
     * @see #toJsonOrThrow(Object)
     */
    String toJson(Object object);

    /**
     * 将对象序列化为 JSON 字符串（抛出异常版本）
     * <p>
     * 如果对象为 null 则返回 null，如果序列化过程中发生异常则抛出异常。
     * </p>
     *
     * @param object 需要序列化的对象，可以为 null
     * @return JSON 字符串，如果对象为 null 则返回 null
     * @throws Exception 如果序列化过程中发生错误
     * @see #toJson(Object)
     */
    String toJsonOrThrow(Object object) throws Exception;

    /**
     * 将 JSON 字符串反序列化为指定类型的对象
     * <p>
     * 如果 JSON 字符串为 null 或空字符串，或反序列化过程中发生异常，则返回 null。
     * </p>
     * <p>
     * 示例：
     * <pre>{@code
     * String json = "{\"name\":\"John\",\"age\":30}";
     * User user = jsonHandler.fromJson(json, User.class);
     * }</pre>
     * </p>
     *
     * @param <T>   目标类型的泛型参数
     * @param json  JSON 字符串，如果为 null 或空字符串则返回 null
     * @param clazz 目标类型的 Class 对象
     * @return 反序列化后的对象，如果解析失败或输入为 null 则返回 null
     * @see #fromJsonOrThrow(String, Class)
     */
    <T> T fromJson(String json, Class<T> clazz);

    /**
     * 将 JSON 字符串反序列化为指定类型的对象（抛出异常版本）
     * <p>
     * 如果 JSON 字符串为 null 或空字符串，或反序列化过程中发生异常，则抛出异常。
     * </p>
     *
     * @param <T>   目标类型的泛型参数
     * @param json  JSON 字符串，不能为 null 或空字符串
     * @param clazz 目标类型的 Class 对象，不能为 null
     * @return 反序列化后的对象
     * @throws Exception 如果 json 为 null 或空字符串，或者 JSON 解析失败
     * @see #fromJson(String, Class)
     */
    <T> T fromJsonOrThrow(String json, Class<T> clazz) throws Exception;

    /**
     * 将 JSON 字符串反序列化为指定 Type 类型的对象
     * <p>
     * 使用 Type 支持更灵活的类型指定方式，特别是对于泛型类型。
     * 如果 JSON 字符串为 null 或空字符串，或 type 为 null，或反序列化过程中发生异常，则返回 null。
     * </p>
     * <p>
     * 示例：
     * <pre>{@code
     * // 反序列化为 List<User>
     * String json = "[{\"name\":\"John\"},{\"name\":\"Alice\"}]";
     * Type listType = new TypeToken<List<User>>() {}.getType();
     * List<User> users = jsonHandler.fromJson(json, listType);
     * }</pre>
     * </p>
     *
     * @param <T>  目标类型的泛型参数
     * @param json JSON 字符串，如果为 null 或空字符串则返回 null
     * @param type 目标类型的 Type 对象，如果为 null 则返回 null
     * @return 反序列化后的对象，如果解析失败或输入为 null 则返回 null
     * @see #fromJsonOrThrow(String, Type)
     */
    <T> T fromJson(String json, Type type);

    /**
     * 将 JSON 字符串反序列化为指定 Type 类型的对象（抛出异常版本）
     * <p>
     * 使用 Type 支持更灵活的类型指定方式。
     * 如果 JSON 字符串为 null 或空字符串，或 type 为 null，则抛出异常。
     * 如果反序列化过程中发生异常，则抛出异常。
     * </p>
     *
     * @param <T>  目标类型的泛型参数
     * @param json JSON 字符串，不能为 null 或空字符串
     * @param type 目标类型的 Type 对象，不能为 null
     * @return 反序列化后的对象
     * @throws Exception 如果 json 为 null 或空字符串，或者 type 为 null，或者 JSON 解析失败
     * @see #fromJson(String, Type)
     */
    <T> T fromJsonOrThrow(String json, Type type) throws Exception;

    /**
     * 将 JSON 字符串转换为指定元素类型的 List
     * <p>
     * 如果 JSON 字符串为 null 或空字符串，或 clazz 为 null，或反序列化过程中发生异常，则返回 null。
     * </p>
     * <p>
     * 示例：
     * <pre>{@code
     * String json = "[{\"name\":\"John\"},{\"name\":\"Alice\"}]";
     * List<User> users = jsonHandler.toList(json, User.class);
     * }</pre>
     * </p>
     *
     * @param <E>   列表元素的类型
     * @param json  JSON 字符串，如果为 null 或空字符串则返回 null
     * @param clazz 列表元素的 Class 对象，如果为 null 则返回 null
     * @return 包含指定类型元素的 List，如果解析失败或输入为 null 则返回 null
     * @see #toListOrThrow(String, Class)
     */
    <E> List<E> toList(String json, Class<E> clazz);

    /**
     * 将 JSON 字符串转换为指定元素类型的 List（抛出异常版本）
     * <p>
     * 如果 JSON 字符串为 null 或空字符串，或 clazz 为 null，或反序列化过程中发生异常，则抛出异常。
     * </p>
     *
     * @param <E>   列表元素的类型
     * @param json  JSON 字符串，不能为 null 或空字符串
     * @param clazz 列表元素的 Class 对象，不能为 null
     * @return 包含指定类型元素的 List
     * @throws Exception 如果 json 为 null 或空字符串，或者 clazz 为 null，或者 JSON 解析失败
     * @see #toList(String, Class)
     */
    <E> List<E> toListOrThrow(String json, Class<E> clazz) throws Exception;

    /**
     * 将 JSON 字符串转换为指定键值类型的 Map
     * <p>
     * 如果 JSON 字符串为 null 或空字符串，或 keyClass/valueClass 为 null，或反序列化过程中发生异常，则返回 null。
     * </p>
     * <p>
     * 示例：
     * <pre>{@code
     * String json = "{\"user1\":{\"name\":\"John\"},\"user2\":{\"name\":\"Alice\"}}";
     * Map<String, User> userMap = jsonHandler.toMap(json, String.class, User.class);
     * }</pre>
     * </p>
     *
     * @param <K>        键的类型
     * @param <V>        值的类型
     * @param json       JSON 字符串，如果为 null 或空字符串则返回 null
     * @param keyClass   键的 Class 对象，如果为 null 则返回 null
     * @param valueClass 值的 Class 对象，如果为 null 则返回 null
     * @return 包含指定键值类型的 Map，如果解析失败或输入为 null 则返回 null
     * @see #toMapOrThrow(String, Class, Class)
     */
    <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass);

    /**
     * 将 JSON 字符串转换为指定键值类型的 Map（抛出异常版本）
     * <p>
     * 如果 JSON 字符串为 null 或空字符串，或 keyClass/valueClass 为 null，或反序列化过程中发生异常，则抛出异常。
     * </p>
     *
     * @param <K>        键的类型
     * @param <V>        值的类型
     * @param json       JSON 字符串，不能为 null 或空字符串
     * @param keyClass   键的 Class 对象，不能为 null
     * @param valueClass 值的 Class 对象，不能为 null
     * @return 包含指定键值类型的 Map
     * @throws Exception 如果 json 为 null 或空字符串，或者 keyClass/valueClass 为 null，或者 JSON 解析失败
     * @see #toMap(String, Class, Class)
     */
    <K, V> Map<K, V> toMapOrThrow(String json, Class<K> keyClass, Class<V> valueClass) throws Exception;

    /**
     * 将对象序列化为格式化的 JSON 字符串
     * <p>
     * 生成易读的格式化 JSON 字符串。
     * 如果对象为 null 或序列化过程中发生异常，则返回 null 或非格式化的 JSON 字符串。
     * </p>
     * <p>
     * 示例：
     * <pre>{@code
     * User user = new User("John", 30);
     * String prettyJson = jsonHandler.toPrettyJson(user);
     * // 结果:
     * // {
     * //   "name" : "John",
     * //   "age" : 30
     * // }
     * }</pre>
     * </p>
     *
     * @param object 需要序列化的对象，可以为 null
     * @return 格式化后的 JSON 字符串，如果对象为 null 则返回 null，如果格式化失败则返回非格式化的 JSON 字符串
     * @see #toJson(Object)
     */
    String toPrettyJson(Object object);

    /**
     * 检查字符串是否为有效的 JSON 格式
     * <p>
     * 此方法会尝试将字符串解析为 JSON，如果解析成功则返回 true，否则返回 false。
     * 空字符串或 null 会被视为无效 JSON。
     * </p>
     * <p>
     * 示例：
     * <pre>{@code
     * boolean valid1 = jsonHandler.isValidJson("{\"name\":\"John\"}"); // true
     * boolean valid2 = jsonHandler.isValidJson("not a json");          // false
     * boolean valid3 = jsonHandler.isValidJson("");                    // false
     * boolean valid4 = jsonHandler.isValidJson(null);                  // false
     * }</pre>
     * </p>
     *
     * @param json 需要检查的字符串，可以为 null
     * @return 如果字符串是有效的 JSON 格式则返回 true，否则返回 false
     */
    boolean isValidJson(String json);
}
