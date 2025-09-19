package com.mytlx.arcane.utils.json.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.Separators;
import com.fasterxml.jackson.databind.*;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Jackson JSON 工具类
 * <p>
 * 提供 JSON 序列化和反序列化的便捷方法，基于 Jackson 库实现。
 * 支持泛型类型的序列化和反序列化，包含安全（返回 null）和抛出异常两种风格的 API。
 * </p>
 * <p>
 * 特性：
 * <ul>
 *   <li>支持泛型类型的序列化和反序列化</li>
 *   <li>提供安全（返回 null）和抛出异常两种风格的 API</li>
 *   <li>支持 List、Map 等集合类型的转换</li>
 *   <li>自动处理日期格式（yyyy-MM-dd HH:mm:ss）</li>
 *   <li>忽略未知 JSON 属性</li>
 *   <li>允许空对象序列化</li>
 * </ul>
 * </p>
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-19 11:21:49
 */
@Slf4j
@UtilityClass
public class JacksonUtils {

    /**
     * Jackson 的 ObjectMapper 实例，已配置常用设置
     * <p>
     * 配置说明：
     * <ul>
     *   <li>日期格式：yyyy-MM-dd HH:mm:ss</li>
     *   <li>忽略未知 JSON 属性</li>
     *   <li>允许空对象序列化</li>
     *   <li>美化输出（缩进）</li>
     * </ul>
     * </p>
     */
    @Getter
    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        // 时间格式
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 忽略未知字段
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许空对象
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 美化输出（可选）
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setDefaultPrettyPrinter(
                new DefaultPrettyPrinter()
                        // 冒号后一个空格
                        .withSeparators(new Separators().withObjectFieldValueSpacing(Separators.Spacing.AFTER))
                        // 不换行不缩进
                        .withObjectIndenter(DefaultPrettyPrinter.NopIndenter.instance)
        );
    }

    // ---------------------- 序列化 ----------------------

    /**
     * 将对象序列化为 JSON 字符串
     * <p>
     * 如果对象为 null 或序列化过程中发生异常，则返回 null 并记录错误日志。
     * </p>
     * <p>
     * 示例：
     * <pre>{@code
     * User user = new User("John", 30);
     * String json = JacksonUtils.toJson(user);
     * // 结果: {"name":"John","age":30}
     * }</pre>
     * </p>
     *
     * @param object 需要序列化的对象，可以为 null
     * @return JSON 字符串，如果对象为 null 或序列化失败则返回 null
     * @see #toJsonOrThrow(Object)
     */
    public static String toJson(Object object) {
        if (object == null) return null;
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("object serialize error, {}", object, e);
            return null;
        }
    }

    /**
     * 将对象序列化为 JSON 字符串（抛出异常版本）
     * <p>
     * 示例：
     * <pre>{@code
     * try {
     *     User user = new User("John", 30);
     *     String json = JacksonUtils.toJsonOrThrow(user);
     *     // 处理 json 字符串
     * } catch (IllegalArgumentException ei) {
     *     // 入参为空
     * } catch (JsonProcessingException ej) {
     *     // 处理序列化异常
     * }
     * }</pre>
     * </p>
     *
     * @param object 需要序列化的对象，可以为 null
     * @return JSON 字符串，如果对象为 null 则返回 null
     * @throws IllegalArgumentException 入参为空
     * @throws JsonProcessingException  如果序列化过程中发生错误
     * @see #toJson(Object)
     */
    public static String toJsonOrThrow(Object object) throws JsonProcessingException {
        if (object == null) throw new IllegalArgumentException("object is null");
        return mapper.writeValueAsString(object);
    }

    // ---------------------- 反序列化 ----------------------

    /**
     * 将 JSON 字符串反序列化为指定类型的对象
     * <p>
     * 如果 JSON 字符串为 null 或空字符串，或反序列化过程中发生异常，则返回 null 并记录错误日志。
     * </p>
     * <p>
     * 示例：
     * <pre>{@code
     * String json = "{\"name\":\"John\",\"age\":30}";
     * User user = JacksonUtils.fromJson(json, User.class);
     * }</pre>
     * </p>
     *
     * @param <T>   目标类型的泛型参数
     * @param json  JSON 字符串，如果为 null 或空字符串则返回 null
     * @param clazz 目标类型的 Class 对象
     * @return 反序列化后的对象，如果解析失败或输入为 null 则返回 null
     * @see #fromJsonOrThrow(String, Class)
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) return null;
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error("json parse error, {}", json, e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的对象（抛出异常版本）
     * <p>
     * 如果 JSON 字符串为 null 或空字符串，则抛出 IllegalArgumentException。
     * 如果反序列化过程中发生异常，则抛出 JsonProcessingException。
     * </p>
     * <p>
     * 示例：
     * <pre>{@code
     * try {
     *     String json = "{\"name\":\"John\",\"age\":30}";
     *     User user = JacksonUtils.fromJsonOrThrow(json, User.class);
     *     // 处理 user 对象
     * } catch (JsonProcessingException e) {
     *     // 处理解析异常
     * } catch (IllegalArgumentException e) {
     *     // 处理参数为空的异常
     * }
     * }</pre>
     * </p>
     *
     * @param <T>   目标类型的泛型参数
     * @param json  JSON 字符串，不能为 null 或空字符串
     * @param clazz 目标类型的 Class 对象，不能为 null
     * @return 反序列化后的对象
     * @throws IllegalArgumentException 如果 json 为 null 或空字符串，或者 clazz 为 null
     * @throws JsonProcessingException  如果 JSON 解析失败
     * @see #fromJson(String, Class)
     */
    public static <T> T fromJsonOrThrow(String json, Class<T> clazz) throws JsonProcessingException {
        if (json == null || json.isEmpty()) throw new IllegalArgumentException("json can't be null or empty");
        return mapper.readValue(json, clazz);
    }

    /**
     * 将 JSON 字符串反序列化为指定泛型类型的对象
     * <p>
     * 使用 TypeReference 支持复杂泛型类型的反序列化。
     * 如果 JSON 字符串为 null 或空字符串，或反序列化过程中发生异常，则返回 null 并记录错误日志。
     * </p>
     * <p>
     * 示例：
     * <pre>{@code
     * // 反序列化为 List<User>
     * String jsonList = "[{\"name\":\"John\"},{\"name\":\"Alice\"}]";
     * List<User> users = JacksonUtils.fromJson(jsonList, new TypeReference<List<User>>() {});
     *
     * // 反序列化为 Map<String, User>
     * String jsonMap = "{\"user1\":{\"name\":\"John\"},\"user2\":{\"name\":\"Alice\"}}";
     * Map<String, User> userMap = JacksonUtils.fromJson(jsonMap, new TypeReference<Map<String, User>>() {});
     * }</pre>
     * </p>
     *
     * @param <T>     目标类型的泛型参数
     * @param json    JSON 字符串，如果为 null 或空字符串则返回 null
     * @param typeRef 指定泛型类型的 TypeReference 对象
     * @return 反序列化后的对象，如果解析失败或输入为 null 则返回 null
     * @see #fromJsonOrThrow(String, TypeReference)
     */
    public static <T> T fromJson(String json, TypeReference<T> typeRef) {
        if (json == null || json.isEmpty()) return null;
        try {
            return mapper.readValue(json, typeRef);
        } catch (Exception e) {
            log.error("json parse error, {}", json, e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串反序列化为指定泛型类型的对象（抛出异常版本）
     * <p>
     * 使用 TypeReference 支持复杂泛型类型的反序列化。
     * 如果 JSON 字符串为 null 或空字符串，则抛出 IllegalArgumentException。
     * 如果反序列化过程中发生异常，则抛出 JsonProcessingException。
     * </p>
     *
     * @param <T>     目标类型的泛型参数
     * @param json    JSON 字符串，不能为 null 或空字符串
     * @param typeRef 指定泛型类型的 TypeReference 对象，不能为 null
     * @return 反序列化后的对象
     * @throws IllegalArgumentException 如果 json 为 null 或空字符串，或者 typeRef 为 null
     * @throws JsonProcessingException  如果 JSON 解析失败
     * @see #fromJson(String, TypeReference)
     */
    public static <T> T fromJsonOrThrow(String json, TypeReference<T> typeRef) throws JsonProcessingException {
        if (json == null || json.isEmpty()) throw new IllegalArgumentException("json can't be null or empty");
        return mapper.readValue(json, typeRef);
    }

    /**
     * 将 JSON 字符串反序列化为指定 Type 类型的对象
     * <p>
     * 使用 Type 支持更灵活的类型指定方式。
     * 如果 JSON 字符串为 null 或空字符串，或 type 为 null，或反序列化过程中发生异常，
     * 则返回 null 并记录错误日志。
     * </p>
     * <p>
     * 示例：
     * <pre>{@code
     * // 反序列化为 List<User>
     * String json = "[{\"name\":\"John\"},{\"name\":\"Alice\"}]";
     * Type listType = new TypeToken<List<User>>() {}.getType();
     * List<User> users = JacksonUtils.fromJson(json, listType);
     * }</pre>
     * </p>
     *
     * @param <T>  目标类型的泛型参数
     * @param json JSON 字符串，如果为 null 或空字符串则返回 null
     * @param type 目标类型的 Type 对象，如果为 null 则返回 null
     * @return 反序列化后的对象，如果解析失败或输入为 null 则返回 null
     * @see #fromJsonOrThrow(String, Type)
     */
    public static <T> T fromJson(String json, Type type) {
        if (json == null || json.isEmpty() || type == null) return null;
        try {
            JavaType javaType = mapper.getTypeFactory().constructType(type);
            return mapper.readValue(json, javaType);
        } catch (Exception e) {
            log.error("Jackson parse(Type) error, {}", json, e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串反序列化为指定 Type 类型的对象（抛出异常版本）
     * <p>
     * 使用 Type 支持更灵活的类型指定方式。
     * 如果 JSON 字符串为 null 或空字符串，或 type 为 null，则抛出 IllegalArgumentException。
     * 如果反序列化过程中发生异常，则抛出 JsonProcessingException。
     * </p>
     *
     * @param <T>  目标类型的泛型参数
     * @param json JSON 字符串，不能为 null 或空字符串
     * @param type 目标类型的 Type 对象，不能为 null
     * @return 反序列化后的对象
     * @throws IllegalArgumentException 如果 json 为 null 或空字符串，或者 type 为 null
     * @throws JsonProcessingException  如果 JSON 解析失败
     * @see #fromJson(String, Type)
     */
    public static <T> T fromJsonOrThrow(String json, Type type) throws JsonProcessingException {
        if (json == null || json.isEmpty() || type == null)
            throw new IllegalArgumentException("json can't be null or empty");
        JavaType javaType = mapper.getTypeFactory().constructType(type);
        return mapper.readValue(json, javaType);
    }

    // ---------------------- List/Map ----------------------

    /**
     * 将 JSON 字符串转换为指定元素类型的 List
     * <p>
     * 如果 JSON 字符串为 null 或空字符串，或反序列化过程中发生异常，
     * 则返回 null 并记录错误日志。
     * </p>
     * <p>
     * 示例：
     * <pre>{@code
     * String json = "[{\"name\":\"John\"},{\"name\":\"Alice\"}]";
     * List<User> users = JacksonUtils.toList(json, User.class);
     * }</pre>
     * </p>
     *
     * @param <E>   列表元素的类型
     * @param json  JSON 字符串，如果为 null 或空字符串则返回 null
     * @param clazz 列表元素的 Class 对象
     * @return 包含指定类型元素的 List，如果解析失败或输入为 null 则返回 null
     * @see #toListOrThrow(String, Class)
     */
    public static <E> List<E> toList(String json, Class<E> clazz) {
        if (json == null || json.isEmpty()) return null;
        try {
            JavaType listType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            return mapper.readValue(json, listType);
        } catch (Exception e) {
            log.error("json to list error, {}", json, e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串转换为指定元素类型的 List（抛出异常版本）
     * <p>
     * 如果 JSON 字符串为 null 或空字符串，则抛出 IllegalArgumentException。
     * 如果反序列化过程中发生异常，则抛出 JsonProcessingException。
     * </p>
     *
     * @param <E>   列表元素的类型
     * @param json  JSON 字符串，不能为 null 或空字符串
     * @param clazz 列表元素的 Class 对象，不能为 null
     * @return 包含指定类型元素的 List
     * @throws IllegalArgumentException 如果 json 为 null 或空字符串，或者 clazz 为 null
     * @throws JsonProcessingException  如果 JSON 解析失败
     * @see #toList(String, Class)
     */
    public static <E> List<E> toListOrThrow(String json, Class<E> clazz) throws JsonProcessingException {
        if (json == null || json.isEmpty()) throw new IllegalArgumentException("json can't be null or empty");
        JavaType listType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
        return mapper.readValue(json, listType);
    }

    /**
     * 将 JSON 字符串转换为指定键值类型的 Map
     * <p>
     * 如果 JSON 字符串为 null 或空字符串，或反序列化过程中发生异常，
     * 则返回 null 并记录错误日志。
     * </p>
     * <p>
     * 示例：
     * <pre>{@code
     * String json = "{\"user1\":{\"name\":\"John\"},\"user2\":{\"name\":\"Alice\"}}";
     * Map<String, User> userMap = JacksonUtils.toMap(json, String.class, User.class);
     * }</pre>
     * </p>
     *
     * @param <K>        键的类型
     * @param <V>        值的类型
     * @param json       JSON 字符串，如果为 null 或空字符串则返回 null
     * @param keyClass   键的 Class 对象
     * @param valueClass 值的 Class 对象
     * @return 包含指定键值类型的 Map，如果解析失败或输入为 null 则返回 null
     * @see #toMapOrThrow(String, Class, Class)
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (json == null || json.isEmpty()) return null;
        try {
            JavaType mapType = mapper.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
            return mapper.readValue(json, mapType);
        } catch (Exception e) {
            log.error("json to map error, {}", json, e);
            return null;
        }
    }

    /**
     * 将 JSON 字符串转换为指定键值类型的 Map（抛出异常版本）
     * <p>
     * 如果 JSON 字符串为 null 或空字符串，则抛出 IllegalArgumentException。
     * 如果反序列化过程中发生异常，则抛出 JsonProcessingException。
     * </p>
     *
     * @param <K>        键的类型
     * @param <V>        值的类型
     * @param json       JSON 字符串，不能为 null 或空字符串
     * @param keyClass   键的 Class 对象，不能为 null
     * @param valueClass 值的 Class 对象，不能为 null
     * @return 包含指定键值类型的 Map
     * @throws IllegalArgumentException 如果 json 为 null 或空字符串，或者 keyClass/valueClass 为 null
     * @throws JsonProcessingException  如果 JSON 解析失败
     * @see #toMap(String, Class, Class)
     */
    public static <K, V> Map<K, V> toMapOrThrow(String json, Class<K> keyClass, Class<V> valueClass) throws JsonProcessingException {
        if (json == null || json.isEmpty()) throw new IllegalArgumentException("json can't be null or empty");
        JavaType mapType = mapper.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
        return mapper.readValue(json, mapType);
    }

    // ---------------------- 其他 ----------------------

    /**
     * 解析 JSON 字符串为 JsonNode 树模型
     * <p>
     * 如果 JSON 字符串为 null 或空字符串，或解析过程中发生异常，
     * 则返回 null 并记录错误日志。
     * </p>
     * <p>
     * 示例：
     * <pre>{@code
     * String json = "{\"name\":\"John\",\"age\":30}";
     * JsonNode node = JacksonUtils.parseJson(json);
     * String name = node.get("name").asText(); // "John"
     * int age = node.get("age").asInt();       // 30
     * }</pre>
     * </p>
     *
     * @param json JSON 字符串，如果为 null 或空字符串则返回 null
     * @return JsonNode 对象，如果解析失败或输入为 null 则返回 null
     * @see com.fasterxml.jackson.databind.JsonNode
     */
    public static JsonNode parseJson(String json) {
        if (json == null || json.isEmpty()) return null;
        try {
            return mapper.readTree(json);
        } catch (Exception e) {
            log.error("json parse error, {}", json, e);
            return null;
        }
    }

    /**
     * 将对象序列化为格式化的 JSON 字符串
     * <p>
     * 如果对象为 null 或序列化过程中发生异常，则返回 null 或非格式化的 JSON 字符串。
     * </p>
     * <p>
     * 示例：
     * <pre>{@code
     * User user = new User("John", 30);
     * String prettyJson = JacksonUtils.toPrettyJson(user);
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
    public static String toPrettyJson(Object object) {
        if (object == null) return null;
        try {
            return mapper.writer(
                            new DefaultPrettyPrinter()
                                    .withSeparators(new Separators().withObjectFieldValueSpacing(Separators.Spacing.AFTER))
                                    .withObjectIndenter(new DefaultIndenter("  ", DefaultIndenter.SYS_LF))
                    )
                    .writeValueAsString(object);
        } catch (Exception e) {
            return toJson(object);
        }
    }

    /**
     * 检查字符串是否为有效的 JSON 格式
     * <p>
     * 此方法会尝试将字符串解析为 JSON 树模型，如果解析成功则返回 true，否则返回 false。
     * 空字符串或 null 会被视为无效 JSON。
     * </p>
     * <p>
     * 示例：
     * <pre>{@code
     * boolean valid1 = JacksonUtils.isValidJson("{\"name\":\"John\"}"); // true
     * boolean valid2 = JacksonUtils.isValidJson("not a json");          // false
     * boolean valid3 = JacksonUtils.isValidJson("");                    // false
     * boolean valid4 = JacksonUtils.isValidJson(null);                  // false
     * }</pre>
     * </p>
     *
     * @param json 需要检查的字符串，可以为 null
     * @return 如果字符串是有效的 JSON 格式则返回 true，否则返回 false
     */
    public static boolean isValidJson(String json) {
        if (json == null || json.isBlank()) return false;
        try {
            mapper.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

