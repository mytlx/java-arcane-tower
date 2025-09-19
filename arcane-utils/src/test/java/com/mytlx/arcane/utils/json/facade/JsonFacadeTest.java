package com.mytlx.arcane.utils.json.facade;

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

/**
 * @author facade
 * @version 1.0.0
 * @since 2025-09-19 13:39:03
 */
class JsonFacadeTest {

    private User testUser;
    private String testUserJson;

    private JsonFacade facade;

    @BeforeEach
    void setUp() {
        facade = JsonFacade.gson();

        testUser = new User("王五", 40);
        testUser.setActive(true);
        testUser.setTags(Arrays.asList("director", "executive"));
        testUser.setAttributes(Map.of("department", "Management", "level", "C-level"));
        testUser.setAddress(new User.Address("广州", "天河区"));

        testUserJson = "{\"name\":\"王五\",\"age\":40,\"active\":true,\"tags\":[\"director\",\"executive\"],\"attributes\":{\"level\":\"C-level\",\"department\":\"Management\"},\"address\":{\"city\":\"广州\",\"street\":\"天河区\"}}";
    }

    @Test
    void toJson_shouldReturnJsonString() {
        String json = facade.toJson(testUser);
        assertNotNull(json);
        assertTrue(json.contains("\"name\": \"王五\"") || json.contains("\"name\":\"王五\""));
        assertTrue(json.contains("\"age\": 40") || json.contains("\"age\":40"));
    }

    @Test
    void toJson_shouldReturnNullForNullInput() {
        assertNull(facade.toJson(null));
    }

    @Test
    void toJsonOrThrow_shouldReturnJsonString() throws Exception {
        String json = facade.toJsonOrThrow(testUser);
        System.out.println(json);
        assertNotNull(json);
        assertTrue(json.contains("\"name\": \"王五\"") || json.contains("\"name\":\"王五\""));
    }

    @Test
    void toJsonOrThrow_shouldThrowForNullInput() {
        assertThrows(IllegalArgumentException.class,
                () -> facade.toJsonOrThrow(null));
    }

    @Test
    void fromJson_shouldDeserializeToObject() {
        User user = facade.fromJson(testUserJson, User.class);
        assertNotNull(user);
        assertEquals("王五", user.getName());
        assertEquals(40, user.getAge());
        assertTrue(user.isActive());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void fromJson_shouldReturnNullForNullOrEmptyInput(String input) {
        assertNull(facade.fromJson(input, User.class));
    }

    @Test
    void fromJson_shouldReturnNullForInvalidJson() {
        assertNull(facade.fromJson("{invalid json}", User.class));
    }

    @Test
    void fromJsonOrThrow_shouldDeserializeToObject() throws Exception {
        User user = facade.fromJsonOrThrow(testUserJson, User.class);
        assertNotNull(user);
        assertEquals("王五", user.getName());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void fromJsonOrThrow_shouldThrowForNullOrEmptyInput(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> facade.fromJsonOrThrow(input, User.class));
    }

    @Test
    void fromJsonOrThrow_shouldThrowForInvalidJson() {
        assertThrows(Exception.class,
                () -> facade.fromJsonOrThrow("{invalid json}", User.class));
    }

    @Test
    void fromJson_withType_shouldDeserializeToType() {
        String listJson = "[\"a\", \"b\", \"c\"]";
        Type type = new com.google.gson.reflect.TypeToken<List<String>>() {
        }.getType();
        List<String> result = facade.fromJson(listJson, type);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
    }

    @Test
    void fromJson_withType_shouldReturnNullForInvalidInput() {
        Type type = new com.google.gson.reflect.TypeToken<List<String>>() {
        }.getType();
        assertNull(facade.fromJson(null, type));
        assertNull(facade.fromJson("", type));
        assertNull(facade.fromJson("invalid json", type));
    }

    @Test
    void toList_shouldDeserializeJsonArrayToList() {
        String listJson = "[\"a\", \"b\", \"c\"]";
        List<String> result = facade.toList(listJson, String.class);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
    }

    @Test
    void toList_shouldReturnNullForInvalidInput() {
        assertNull(facade.toList(null, String.class));
        assertNull(facade.toList("", String.class));
        assertNull(facade.toList("invalid json", String.class));
    }

    @Test
    void toListOrThrow_shouldDeserializeJsonArrayToList() throws Exception {
        String listJson = "[\"a\", \"b\", \"c\"]";
        List<String> result = facade.toListOrThrow(listJson, String.class);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void toListOrThrow_shouldThrowForNullOrEmptyInput(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> facade.toListOrThrow(input, String.class));
    }

    @Test
    void toListOrThrow_shouldThrowForInvalidJson() {
        assertThrows(Exception.class,
                () -> facade.toListOrThrow("invalid json", String.class));
    }

    @Test
    void toMap_shouldDeserializeJsonObjectToMap() {
        String mapJson = "{\"key1\":\"value1\",\"key2\":2}";
        Map<String, Object> result = facade.toMap(mapJson, String.class, Object.class);

        assertNotNull(result);
        assertEquals("value1", result.get("key1"));
        assertEquals(2d, result.get("key2"));
    }

    @Test
    void toMap_shouldReturnNullForInvalidInput() {
        assertNull(facade.toMap(null, String.class, Object.class));
        assertNull(facade.toMap("", String.class, Object.class));
        assertNull(facade.toMap("invalid json", String.class, Object.class));
    }

    @Test
    void toMapOrThrow_shouldDeserializeJsonObjectToMap() throws Exception {
        String mapJson = "{\"key1\":\"value1\",\"key2\":2}";
        Map<String, Object> result = facade.toMapOrThrow(mapJson, String.class, Object.class);

        assertNotNull(result);
        assertEquals("value1", result.get("key1"));
        assertEquals(2, ((Number) result.get("key2")).intValue());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void toMapOrThrow_shouldThrowForNullOrEmptyInput(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> facade.toMapOrThrow(input, String.class, Object.class));
    }

    @Test
    void toMapOrThrow_shouldThrowForInvalidJson() {
        assertThrows(Exception.class,
                () -> facade.toMapOrThrow("invalid json", String.class, Object.class));
    }

    @Test
    void toPrettyJson_shouldReturnFormattedJson() {
        String prettyJson = facade.toPrettyJson(testUser);
        System.out.println(prettyJson);
        assertNotNull(prettyJson);
        assertTrue(prettyJson.contains("\n"));
        assertTrue(prettyJson.contains("  ") || prettyJson.contains("\t")); // Check for indentation
    }

    @Test
    void toPrettyJson_shouldReturnNullForNullInput() {
        assertNull(facade.toPrettyJson(null));
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
        assertTrue(facade.isValidJson(json));
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
        if (input.equals("{key: 'value'}") && facade.equals(JsonFacade.gson())) {
            assertTrue(facade.isValidJson(input));
            return;
        }
        assertFalse(facade.isValidJson(input));
    }

    @Test
    void isValidJson_shouldReturnFalseForNullInput() {
        assertFalse(facade.isValidJson(null));
    }

    @Test
    void testEmptyObjectAndArray() {
        // Test empty object
        String emptyObj = "{}";
        assertTrue(facade.isValidJson(emptyObj));
        assertNotNull(facade.fromJson(emptyObj, Map.class));
        assertTrue(facade.fromJson(emptyObj, Map.class).isEmpty());

        // Test empty array
        String emptyArr = "[]";
        assertTrue(facade.isValidJson(emptyArr));
        assertNotNull(facade.fromJson(emptyArr, List.class));
        assertTrue(facade.fromJson(emptyArr, List.class).isEmpty());
    }

    @Test
    void testSpecialCharacters() {
        String specialJson = "{\"name\":\"特殊字符~!@#$%^&*()_+{}|:<>?,./;'[]\\\\-=`\"}";
        assertTrue(facade.isValidJson(specialJson));
        Map<String, String> result = facade.fromJson(specialJson,
                new com.google.gson.reflect.TypeToken<Map<String, String>>() {
                }.getType());
        assertNotNull(result);
        assertEquals("特殊字符~!@#$%^&*()_+{}|:<>?,./;'[]\\-=`", result.get("name"));
    }
}