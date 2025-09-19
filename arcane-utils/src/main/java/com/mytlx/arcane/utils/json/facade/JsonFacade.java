package com.mytlx.arcane.utils.json.facade;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * JSON 处理工具类（门面模式）
 * <p>
 * 本类是一个门面（Facade）类，提供了统一的 JSON 序列化和反序列化接口，
 * 底层可以透明地切换不同的 JSON 实现（Jackson、Fastjson2、Gson）。
 * </p>
 *
 * <p><b>主要特性：</b></p>
 * <ul>
 *   <li>支持基本对象与 JSON 字符串的相互转换</li>
 *   <li>支持泛型类型的序列化和反序列化</li>
 *   <li>提供安全（返回 null）和抛出异常两种风格的 API</li>
 *   <li>支持 List、Map 等集合类型的转换</li>
 *   <li>支持 JSON 格式验证</li>
 *   <li>支持美化输出（格式化）的 JSON 字符串</li>
 * </ul>
 *
 * <p><b>使用示例：</b></p>
 * <pre>{@code
 * // 1. 基本使用（默认使用 Jackson 实现）
 * User user = new User("张三", 25);
 *
 * // 序列化对象为 JSON 字符串
 * String json = JsonUtils.toJson(user);
 * // 输出: {"name":"张三","age":25}
 *
 * // 反序列化 JSON 字符串为对象
 * User user2 = JsonUtils.fromJson(json, User.class);
 *
 * // 2. 切换 JSON 实现
 * JsonUtils.use(JsonUtils.Engine.GSON);  // 切换到 Gson 实现
 *
 * // 3. 处理集合类型
 * List<User> users = Arrays.asList(new User("张三", 25), new User("李四", 30));
 * String jsonArray = JsonUtils.toJson(users);
 *
 * // 4. 使用 OrThrow 方法（遇到错误时抛出异常）
 * try {
 *     String json = "{\"name\":\"张三\",\"age\":25}";
 *     User user = JsonUtils.fromJsonOrThrow(json, User.class);
 * } catch (Exception e) {
 *     // 处理异常
 * }
 *
 * // 5. 格式化输出
 * String prettyJson = JsonUtils.toPrettyJson(user);
 * // 输出:
 * // {
 * //   "name" : "张三",
 * //   "age" : 25
 * // }
 * }</pre>
 *
 * <p><b>线程安全：</b> 本类的方法都是线程安全的。</p>
 *
 * @author TLX
 * @version 1.0.0
 * @see JsonHandler
 * @see JacksonHandler
 * @see Fastjson2Handler
 * @see GsonHandler
 * @since 2025-09-19 11:45:21
 */
public class JsonFacade {
    public enum Engine {
        JACKSON, FASTJSON2, GSON
    }

    private final JsonHandler handler;

    private JsonFacade(JsonHandler handler) {
        this.handler = handler;
    }

    // 静态单例实例
    private static final JsonFacade JACKSON_INSTANCE = new JsonFacade(new JacksonHandler());
    private static final JsonFacade FASTJSON2_INSTANCE = new JsonFacade(new Fastjson2Handler());
    private static final JsonFacade GSON_INSTANCE = new JsonFacade(new GsonHandler());


    public static JsonFacade use(JsonFacade.Engine engine) {
        return switch (engine) {
            case JACKSON -> JACKSON_INSTANCE;
            case FASTJSON2 -> FASTJSON2_INSTANCE;
            case GSON -> GSON_INSTANCE;
        };
    }

    public static JsonFacade def(){
        return JsonFacade.JACKSON_INSTANCE;
    }

    public static JsonFacade jackson() {
        return JACKSON_INSTANCE;
    }

    public static JsonFacade fastjson2() {
        return FASTJSON2_INSTANCE;
    }

    public static JsonFacade gson() {
        return GSON_INSTANCE;
    }

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
     *
     * // 处理循环引用（取决于底层实现）
     * user.setFriend(user);  // 自引用
     * String circularRefJson = JsonUtils.toJson(user);
     * }</pre>
     *
     * @param obj 需要序列化的对象，可以为 {@code null}
     * @return JSON 字符串，如果对象为 {@code null} 或序列化失败则返回 {@code null}
     * @see #toJsonOrThrow(Object)
     */
    public String toJson(Object obj) {
        return handler.toJson(obj);
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
     *
     *     // 处理循环引用（取决于底层实现）
     *     user.setFriend(user);  // 自引用
     *     String circularRefJson = JsonUtils.toJsonOrThrow(user);
     * } catch (Exception e) {
     *     // 处理序列化错误
     *     logger.error("序列化对象失败", e);
     * }
     * }</pre>
     *
     * @param obj 需要序列化的对象
     * @return JSON 字符串
     * @throws IllegalArgumentException            如果对象为 {@code null}
     * @throws JsonProcessingException             如果序列化过程中发生 JSON 处理错误
     * @throws com.alibaba.fastjson2.JSONException 如果使用 Fastjson2 实现时发生错误
     * @throws com.google.gson.JsonParseException  如果使用 Gson 实现时发生错误
     * @see #toJson(Object)
     */
    public String toJsonOrThrow(Object obj) throws Exception {
        return handler.toJsonOrThrow(obj);
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
     * User emptyUser = JsonUtils.fromJson("", User.class);   // 返回 null
     * }</pre>
     *
     * @param <T>   目标对象类型
     * @param json  JSON 字符串，可以为 {@code null}
     * @param clazz 目标对象类型的 Class 对象
     * @return 反序列化后的对象，如果 JSON 字符串为 {@code null} 或反序列化失败则返回 {@code null}
     * @see #fromJsonOrThrow(String, Class)
     */
    public <T> T fromJson(String json, Class<T> clazz) {
        return handler.fromJson(json, clazz);
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
     *     // 以下情况会抛出异常：
     *     // 1. JSON 为 null 或空
     *     // User nullUser = JsonUtils.fromJsonOrThrow(null, User.class);
     *
     *     // 2. JSON 格式错误
     *     // User invalid = JsonUtils.fromJsonOrThrow("invalid json", User.class);
     *
     *     // 3. 类型不匹配
     *     // Integer number = JsonUtils.fromJsonOrThrow("\"not a number\"", Integer.class);
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
     * @throws IllegalArgumentException            如果 JSON 字符串为 {@code null} 或空，或者 clazz 为 {@code null}
     * @throws JsonProcessingException             如果反序列化过程中发生 JSON 处理错误
     * @throws com.alibaba.fastjson2.JSONException 如果使用 Fastjson2 实现时发生错误
     * @throws com.google.gson.JsonParseException  如果使用 Gson 实现时发生错误
     * @see #fromJson(String, Class)
     */
    public <T> T fromJsonOrThrow(String json, Class<T> clazz) throws Exception {
        return handler.fromJsonOrThrow(json, clazz);
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的对象
     * <p>
     * 支持泛型类型的反序列化，例如 {@code List<User>} 或 {@code Map<String, User>}。
     * 如果 JSON 字符串为 {@code null}、空或反序列化过程中发生异常，则返回 {@code null}。
     * 不会抛出异常，错误信息会记录到日志中。
     * </p>
     *
     * <p><b>示例：</b></p>
     * <pre>{@code
     * // 1. 反序列化为 List
     * String listJson = "[{\"name\":\"张三\"},{\"name\":\"李四\"}]";
     * // 1.1 gson fastjson
     * Type listType1 = new TypeToken<List<User>>() {}.getType();
     * // 1.2 Jackson fastjson
     * Type listType2 = new TypeReference<List<User>>() {}.getType();
     * List<User> userList = JsonUtils.fromJson(listJson, listType);
     *
     * // 2. 反序列化为 Map
     * String mapJson = "{\"zhangsan\":{\"name\":\"张三\"},\"lisi\":{\"name\":\"李四\"}}";
     * // 2.1 gson fastjson
     * Type mapType1 = new TypeToken<Map<String, User>>() {}.getType();
     * // 2.2 Jackson fastjson
     * Type mapType2 = new TypeReference<Map<String, User>>() {}.getType();
     * Map<String, User> userMap = JsonUtils.fromJson(mapJson, mapType);
     *
     * // 3. 处理复杂泛型类型
     * String complexJson = "{\"data\":[{\"key\":1,\"value\":\"A\"}]}";
     * // 3.1 gson fastjson
     * Type complexType1 = new TypeToken<Response<List<Entry<Integer, String>>>>() {}.getType();
     * // 3.2 Jackson fastjson
     * Type complexType2 = new TypeReference<Response<List<Entry<Integer, String>>>>() {}.getType();
     * Response<List<Entry<Integer, String>>> result = JsonUtils.fromJson(complexJson, complexType);
     * }</pre>
     *
     * @param <T>  目标对象类型
     * @param json JSON 字符串，可以为 {@code null}
     * @param type 目标对象类型的 Type 对象，通常通过 {@code TypeToken} 获取
     * @return 反序列化后的对象，如果 JSON 字符串为 {@code null} 或反序列化失败则返回 {@code null}
     * @see #fromJsonOrThrow(String, Type)
     * @see com.google.gson.reflect.TypeToken
     * @see com.fasterxml.jackson.core.type.TypeReference
     */
    public <T> T fromJson(String json, Type type) {
        return handler.fromJson(json, type);
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的对象（抛出异常版本）
     * <p>
     * 支持泛型类型的反序列化，例如 {@code List<User>} 或 {@code Map<String, User>}。
     * 如果 JSON 字符串为 {@code null}、空或反序列化过程中发生异常，则抛出相应的异常。
     * 适用于需要严格处理反序列化错误的场景。
     * </p>
     *
     * @param <T>  目标对象类型
     * @param json JSON 字符串
     * @param type 目标对象类型的 Type 对象，通常通过 {@code TypeToken} 获取
     * @return 反序列化后的对象
     * @throws IllegalArgumentException            如果 JSON 字符串为 {@code null} 或空，或者 type 为 {@code null}
     * @throws JsonProcessingException             如果反序列化过程中发生 JSON 处理错误
     * @throws com.alibaba.fastjson2.JSONException 如果使用 Fastjson2 实现时发生错误
     * @throws com.google.gson.JsonParseException  如果使用 Gson 实现时发生错误
     * @see #fromJson(String, Type)
     * @see com.google.gson.reflect.TypeToken
     * @see com.fasterxml.jackson.core.type.TypeReference
     */
    public <T> T fromJsonOrThrow(String json, Type type) throws Exception {
        return handler.fromJsonOrThrow(json, type);
    }

    /**
     * 将 JSON 数组字符串反序列化为指定元素类型的列表
     * <p>
     * 如果 JSON 字符串为 {@code null}、空或反序列化过程中发生异常，则返回 {@code null}。
     * 不会抛出异常，错误信息会记录到日志中。
     * </p>
     *
     * <p><b>示例：</b></p>
     * <pre>{@code
     * // 1. 反序列化简单列表
     * String json = "[\"A\", \"B\", \"C\"]";
     * List<String> strings = JsonUtils.toList(json, String.class);
     *
     * // 2. 反序列化对象列表
     * String usersJson = "[{\"name\":\"张三\"}, {\"name\":\"李四\"}]";
     * List<User> users = JsonUtils.toList(usersJson, User.class);
     *
     * // 3. 处理无效输入
     * List<User> invalid = JsonUtils.toList("not a json array", User.class);  // 返回 null
     * List<User> empty = JsonUtils.toList("", User.class);  // 返回 null
     * List<User> nullList = JsonUtils.toList(null, User.class);  // 返回 null
     * }</pre>
     *
     * @param <E>   列表元素类型
     * @param json  JSON 数组字符串，例如 {@code [{"name":"Alice"},{"name":"Bob"}]}
     * @param clazz 列表元素类型的 Class 对象
     * @return 包含反序列化后对象的列表，如果 JSON 为 {@code null}、空或反序列化失败则返回 {@code null}
     * @see #toListOrThrow(String, Class)
     */
    public <E> List<E> toList(String json, Class<E> clazz) {
        return handler.toList(json, clazz);
    }

    /**
     * 将 JSON 数组字符串反序列化为指定元素类型的列表（抛出异常版本）
     * <p>
     * 如果 JSON 字符串为 {@code null}、空或反序列化过程中发生异常，则抛出相应的异常。
     * 适用于需要严格处理反序列化错误的场景。
     * </p>
     *
     * @param <E>   列表元素类型
     * @param json  JSON 数组字符串，例如 {@code [{"name":"Alice"},{"name":"Bob"}]}
     * @param clazz 列表元素类型的 Class 对象
     * @return 包含反序列化后对象的列表
     * @throws IllegalArgumentException            如果 JSON 字符串为 {@code null} 或空，或者 clazz 为 {@code null}
     * @throws JsonProcessingException             如果反序列化过程中发生 JSON 处理错误
     * @throws com.alibaba.fastjson2.JSONException 如果使用 Fastjson2 实现时发生错误
     * @throws com.google.gson.JsonParseException  如果使用 Gson 实现时发生错误
     * @see #toList(String, Class)
     */
    public <E> List<E> toListOrThrow(String json, Class<E> clazz) throws Exception {
        return handler.toListOrThrow(json, clazz);
    }

    /**
     * 将 JSON 对象字符串反序列化为指定键值类型的映射
     * <p>
     * 如果 JSON 字符串为 {@code null}、空或反序列化过程中发生异常，则返回 {@code null}。
     * 不会抛出异常，错误信息会记录到日志中。
     * </p>
     *
     * <p><b>示例：</b></p>
     * <pre>{@code
     * // 1. 反序列化为 Map<String, String>
     * String stringMapJson = "{\"key1\":\"value1\",\"key2\":\"value2\"}";
     * Map<String, String> stringMap = JsonUtils.toMap(stringMapJson, String.class, String.class);
     *
     * // 2. 反序列化为 Map<Integer, User>
     * String userMapJson = "{\"1\":{\"name\":\"张三\"},\"2\":{\"name\":\"李四\"}}";
     * Map<Integer, User> userMap = JsonUtils.toMap(userMapJson, Integer.class, User.class);
     *
     * // 3. 处理无效输入
     * Map<String, String> invalid = JsonUtils.toMap("not a json object", String.class, String.class);  // 返回 null
     * Map<String, String> empty = JsonUtils.toMap("", String.class, String.class);  // 返回 null
     * Map<String, String> nullMap = JsonUtils.toMap(null, String.class, String.class);  // 返回 null
     * }</pre>
     *
     * @param <K>        映射键类型
     * @param <V>        映射值类型
     * @param json       JSON 对象字符串，例如 {"1":{"name":"Alice"},"2":{"name":"Bob"}}
     * @param keyClass   键类型的 Class 对象
     * @param valueClass 值类型的 Class 对象
     * @return 包含反序列化后键值对的映射，如果 JSON 为 {@code null}、空或反序列化失败则返回 {@code null}
     * @see #toMapOrThrow(String, Class, Class)
     */
    public <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
        return handler.toMap(json, keyClass, valueClass);
    }

    /**
     * 将 JSON 对象字符串反序列化为指定键值类型的映射（抛出异常版本）
     * <p>
     * 如果 JSON 字符串为 {@code null}、空或反序列化过程中发生异常，则抛出相应的异常。
     * 适用于需要严格处理反序列化错误的场景。
     * </p>
     *
     * @param <K>        映射键类型
     * @param <V>        映射值类型
     * @param json       JSON 对象字符串，例如 {"1":{"name":"Alice"},"2":{"name":"Bob"}}
     * @param keyClass   键类型的 Class 对象
     * @param valueClass 值类型的 Class 对象
     * @return 包含反序列化后键值对的映射
     * @throws IllegalArgumentException            如果 JSON 字符串为 {@code null} 或空，或者 keyClass/valueClass 为 {@code null}
     * @throws JsonProcessingException             如果反序列化过程中发生 JSON 处理错误
     * @throws com.alibaba.fastjson2.JSONException 如果使用 Fastjson2 实现时发生错误
     * @throws com.google.gson.JsonParseException  如果使用 Gson 实现时发生错误
     * @see #toMap(String, Class, Class)
     */
    public <K, V> Map<K, V> toMapOrThrow(String json, Class<K> keyClass, Class<V> valueClass) throws Exception {
        return handler.toMapOrThrow(json, keyClass, valueClass);
    }

    /**
     * 将对象格式化为易读的 JSON 字符串
     * <p>
     * 生成的 JSON 字符串会进行格式化，包含缩进和换行，适合阅读和日志输出。
     * 如果对象为 {@code null} 或格式化过程中发生异常，则返回 {@code null}。
     * 不会抛出异常，错误信息会记录到日志中。
     * </p>
     *
     * <p><b>示例：</b></p>
     * <pre>{@code
     * User user = new User("张三", 25);
     * user.setAddress(new Address("北京市", "海淀区"));
     *
     * String prettyJson = JsonUtils.toPrettyJson(user);
     * // 输出:
     * // {
     * //   "name" : "张三",
     * //   "age" : 25,
     * //   "address" : {
     * //     "city" : "北京市",
     * //     "district" : "海淀区"
     * //   }
     * // }
     *
     * // 处理 null 值
     * String nullResult = JsonUtils.toPrettyJson(null);  // 返回 null
     * }</pre>
     *
     * @param obj 需要格式化的对象，可以为 {@code null}
     * @return 格式化后的 JSON 字符串，如果对象为 {@code null} 或格式化失败则返回 {@code null}
     */
    public String toPrettyJson(Object obj) {
        return handler.toPrettyJson(obj);
    }

    /**
     * 验证字符串是否为有效的 JSON 格式
     * <p>
     * 该方法会检查字符串是否可以成功解析为 JSON 对象或数组。
     * 如果字符串为 {@code null} 或空，则返回 {@code false}。
     * 不会抛出异常，错误信息会记录到日志中。
     * </p>
     *
     * <p><b>示例：</b></p>
     * <pre>{@code
     * // 验证有效 JSON
     * boolean isValid1 = JsonUtils.isValidJson("{\"name\":\"张三\"}");  // 返回 true
     * boolean isValid2 = JsonUtils.isValidJson("[1, 2, 3]");  // 返回 true
     *
     * // 验证无效 JSON
     * boolean isInvalid1 = JsonUtils.isValidJson("not a json");  // 返回 false
     * boolean isInvalid2 = JsonUtils.isValidJson("{name: 张三}");  // 返回 false（键需要引号）
     *
     * // 处理边界情况
     * boolean isNull = JsonUtils.isValidJson(null);  // 返回 false
     * boolean isEmpty = JsonUtils.isValidJson("");    // 返回 false
     * }</pre>
     *
     * @param json 需要验证的字符串，可以为 {@code null}
     * @return 如果字符串是有效的 JSON 格式则返回 {@code true}，否则返回 {@code false}
     */
    public boolean isValidJson(String json) {
        return handler.isValidJson(json);
    }
}
