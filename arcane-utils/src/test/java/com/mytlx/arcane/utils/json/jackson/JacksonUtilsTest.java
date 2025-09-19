package com.mytlx.arcane.utils.json.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mytlx.arcane.utils.json.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JacksonUtilsTest {

    private User testUser;
    private String testUserJson;

    @BeforeEach
    void setUp() {
        testUser = new User("李四", 35);
        testUser.setActive(false);
        testUser.setTags(Arrays.asList("manager", "lead"));
        testUser.setAttributes(Map.of("department", "HR", "level", "senior"));
        testUser.setAddress(new User.Address("上海", "南京路"));

        testUserJson = "{\"name\":\"李四\",\"age\":35,\"active\":false,\"tags\":[\"manager\",\"lead\"],\"attributes\":{\"level\":\"senior\",\"department\":\"HR\"},\"address\":{\"city\":\"上海\",\"street\":\"南京路\"}}";
    }

    @Test
    void toJson_shouldReturnJsonString() {
        String json = JacksonUtils.toJson(testUser);
        assertNotNull(json);
        assertTrue(json.contains("\"name\": \"李四\""));
        assertTrue(json.contains("\"age\": 35"));
    }

    @Test
    void toJson_shouldReturnNullForNullInput() {
        assertNull(JacksonUtils.toJson(null));
    }

    @Test
    void toJsonOrThrow_shouldReturnJsonString() throws JsonProcessingException {
        String json = JacksonUtils.toJsonOrThrow(testUser);
        System.out.println(json);
        assertNotNull(json);
        assertTrue(json.contains("\"name\": \"李四\""));
    }

    @Test
    void toJsonOrThrow_shouldThrowForNullInput() {
        assertThrows(IllegalArgumentException.class,
                () -> JacksonUtils.toJsonOrThrow(null));
    }

    @Test
    void fromJson_shouldDeserializeToObject() {
        User user = JacksonUtils.fromJson(testUserJson, User.class);
        assertNotNull(user);
        assertEquals("李四", user.getName());
        assertEquals(35, user.getAge());
        assertFalse(user.isActive());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void fromJson_shouldReturnNullForNullOrEmptyInput(String input) {
        assertNull(JacksonUtils.fromJson(input, User.class));
    }

    @Test
    void fromJson_shouldReturnNullForInvalidJson() {
        assertNull(JacksonUtils.fromJson("{invalid json}", User.class));
    }

    @Test
    void fromJsonOrThrow_shouldDeserializeToObject() throws JsonProcessingException {
        User user = JacksonUtils.fromJsonOrThrow(testUserJson, User.class);
        assertNotNull(user);
        assertEquals("李四", user.getName());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void fromJsonOrThrow_shouldThrowForNullOrEmptyInput(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> JacksonUtils.fromJsonOrThrow(input, User.class));
    }

    @Test
    void fromJsonOrThrow_shouldThrowForInvalidJson() {
        assertThrows(JsonProcessingException.class,
                () -> JacksonUtils.fromJsonOrThrow("{invalid json}", User.class));
    }

    @Test
    void fromJson_withTypeReference_shouldDeserializeToGenericType() {
        String listJson = "[\"x\", \"y\", \"z\"]";
        List<String> result = JacksonUtils.fromJson(listJson, new TypeReference<List<String>>() {});
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("x", result.getFirst());
    }

    @Test
    void fromJson_withTypeReference_shouldReturnNullForInvalidInput() {
        assertNull(JacksonUtils.fromJson(null, new TypeReference<List<String>>() {}));
        assertNull(JacksonUtils.fromJson("", new TypeReference<List<String>>() {}));
        assertNull(JacksonUtils.fromJson("invalid json", new TypeReference<List<String>>() {}));
    }

    @Test
    void fromJson_withType_shouldDeserializeToType() {
        String mapJson = "{\"a\":1,\"b\":2}";
        Type type = new TypeReference<Map<String, Integer>>() {}.getType();
        Map<String, Integer> result = JacksonUtils.fromJson(mapJson, type);

        assertNotNull(result);
        assertEquals(1, result.get("a"));
        assertEquals(2, result.get("b"));
    }

    @Test
    void fromJson_withType_shouldReturnNullForInvalidInput() {
        Type type = new TypeReference<Map<String, String>>() {}.getType();
        assertNull(JacksonUtils.fromJson(null, type));
        assertNull(JacksonUtils.fromJson("", type));
        assertNull(JacksonUtils.fromJson("invalid json", type));
    }

    @Test
    void toPrettyJson_shouldReturnFormattedJson() {
        String prettyJson = JacksonUtils.toPrettyJson(testUser);
        System.out.println(prettyJson);
        assertNotNull(prettyJson);
        assertTrue(prettyJson.contains("\n"));
        assertTrue(prettyJson.contains("  ")); // Check for indentation
    }

    @Test
    void toPrettyJson_shouldReturnNullForNullInput() {
        assertNull(JacksonUtils.toPrettyJson(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"key\":\"value\"}",
            "[1, 2, 3]",
            "123",
            "\"string\"",
            "true",
            "null"
    })
    void isValidJson_shouldReturnTrueForValidJson(String json) {
        assertTrue(JacksonUtils.isValidJson(json));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "  ",
            "{key: 'value'}",
            "[1, 2, 3",
            "not a json"
    })
    void isValidJson_shouldReturnFalseForInvalidJson(String input) {
        assertFalse(JacksonUtils.isValidJson(input));
    }

    @Test
    void isValidJson_shouldReturnFalseForNullInput() {
        assertFalse(JacksonUtils.isValidJson(null));
    }

    @Test
    void testEmptyObjectAndArray() {
        // Test empty object
        String emptyObj = "{}";
        assertTrue(JacksonUtils.isValidJson(emptyObj));
        assertNotNull(JacksonUtils.fromJson(emptyObj, Map.class));
        assertTrue(JacksonUtils.fromJson(emptyObj, Map.class).isEmpty());

        // Test empty array
        String emptyArr = "[]";
        assertTrue(JacksonUtils.isValidJson(emptyArr));
        assertNotNull(JacksonUtils.fromJson(emptyArr, List.class));
        assertTrue(JacksonUtils.fromJson(emptyArr, List.class).isEmpty());
    }

    @Test
    void testSpecialCharacters() {
        String specialJson = "{\"name\":\"特殊字符~!@#$%^&*()_+{}|:<>?,./;'[]\\\\-=`\"}";
        assertTrue(JacksonUtils.isValidJson(specialJson));
        Map<String, String> result = JacksonUtils.fromJson(specialJson, new TypeReference<Map<String, String>>() {});
        assertNotNull(result);
        assertEquals("特殊字符~!@#$%^&*()_+{}|:<>?,./;'[]\\-=`", result.get("name"));
    }
}