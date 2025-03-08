package com.mytlx.json;


import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.mytlx.arcane.utils.json.gson.GsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GsonUtils 单元测试类
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-15 16:45
 */
public class GsonUtilsTest {


    @Test
    public void toJson() {
        // 将对象转换为 JSON 字符串
        Person person = new Person("John", 30);
        String json = GsonUtils.toJson(person);
        System.out.println(json); // 输出: {"name":"John","age":30}
    }

    @Test
    public void fromJson() {
        // 将 JSON 字符串转换为对象
        String json = "{\"name\":\"John\",\"age\":30}";
        Person person = GsonUtils.fromJson(json, Person.class);
        System.out.println(person.getName()); // 输出: John
    }

    @Test
    public void testFromJson() {
        String json = "{\"name\":\"John\",\"age\":30}";
        TypeToken<Person> typeToken = new TypeToken<Person>() {
        };
        Person person = GsonUtils.fromJson(json, typeToken);

        assertNotNull(person, "Person object should not be null");
        assertEquals("John", person.getName(), "Name should be John");
        assertEquals(30, person.getAge(), "Age should be 30");
    }

    @Test
    public void parseJson() {
        String json = "{\"name\":\"John\",\"age\":30}";
        JsonElement jsonElement = GsonUtils.parseJson(json);

        assertNotNull(jsonElement, "JsonElement should not be null");
        assertTrue(jsonElement.isJsonObject(), "Parsed JSON should be a JsonObject");
    }

    @Test
    public void toPrettyJson() {
        Person person = new Person("John", 30);
        String json = GsonUtils.toPrettyJson(person);

        System.out.println("json = " + json);

        assertNotNull(json, "JSON string should not be null");
        assertTrue(json.contains("John"), "JSON should contain 'John'");
        assertTrue(json.contains("30"), "JSON should contain '30'");

    }

    @Test
    public void isValidJson() {
        String validJson = "{\"name\":\"John\",\"age\":30}";
        String invalidJson = "{name:John,age:30}"; // 不正确的 JSON 格式

        assertTrue(GsonUtils.isValidJson(validJson), "Valid JSON should return true");
        assertFalse(GsonUtils.isValidJson(invalidJson), "Invalid JSON should return false");
    }

    @Test
    public void toList() {
        // 将 JSON 字符串转换为 List
        String json = "[{\"name\":\"John\",\"age\":30},{\"name\":\"Jane\",\"age\":25}]";
        List<Person> personList = GsonUtils.toList(json, Person.class);
        System.out.println(personList.size()); // 输出: 2
    }

    @Test
    public void toMap() {
        // 将 JSON 字符串转换为 Map
        String json = "{\"name\":\"John\",\"age\":30}";
        Map<String, Object> personMap = GsonUtils.toMap(json, String.class, Object.class);
        System.out.println(personMap.get("name")); // 输出: John
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Person {
        private String name;
        private int age;
    }

}