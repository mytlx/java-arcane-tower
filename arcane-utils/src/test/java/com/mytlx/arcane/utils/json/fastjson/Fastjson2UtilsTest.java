package com.mytlx.arcane.utils.json.fastjson;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.TypeReference;
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

class Fastjson2UtilsTest {

    private User testUser;
    private String testUserJson;

    @BeforeEach
    void setUp() {
        testUser = new User("张三", 30);
        testUser.setActive(true);
        testUser.setTags(Arrays.asList("developer", "tester"));
        testUser.setAttributes(Map.of("department", "IT", "role", "lead"));
        testUser.setAddress(new User.Address("北京", "中关村大街"));

        testUserJson = "{\"active\":true,\"address\":{\"city\":\"北京\",\"street\":\"中关村大街\"},\"age\":30,\"attributes\":{\"role\":\"lead\",\"department\":\"IT\"},\"name\":\"张三\",\"tags\":[\"developer\",\"tester\"]}";
    }

    // Test toJson
    @Test
    void toJson_shouldReturnJsonString() {
        String json = Fastjson2Utils.toJson(testUser);
        assertNotNull(json);
        assertTrue(json.contains("\"name\":\"张三\""));
        assertTrue(json.contains("\"age\":30"));
    }

    @Test
    void toJson_shouldReturnNullForNullInput() {
        assertNull(Fastjson2Utils.toJson(null));
    }

    // Test toJsonOrThrow
    @Test
    void toJsonOrThrow_shouldReturnJsonString() {
        String json = Fastjson2Utils.toJsonOrThrow(testUser);
        assertNotNull(json);
        assertTrue(json.contains("\"name\":\"张三\""));
    }

    @Test
    void toJsonOrThrow_shouldThrowForNullInput() {
        assertThrows(IllegalArgumentException.class, () -> Fastjson2Utils.toJsonOrThrow(null));
    }

    // Test fromJson with Class
    @Test
    void fromJson_shouldDeserializeToObject() {
        User user = Fastjson2Utils.fromJson(testUserJson, User.class);
        assertNotNull(user);
        assertEquals("张三", user.getName());
        assertEquals(30, user.getAge());
        assertTrue(user.isActive());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void fromJson_shouldReturnNullForNullOrEmptyInput(String input) {
        assertNull(Fastjson2Utils.fromJson(input, User.class));
    }

    @Test
    void fromJson_shouldReturnNullForInvalidJson() {
        assertNull(Fastjson2Utils.fromJson("{invalid json}", User.class));
    }

    // Test fromJsonOrThrow with Class
    @Test
    void fromJsonOrThrow_shouldDeserializeToObject() {
        User user = Fastjson2Utils.fromJsonOrThrow(testUserJson, User.class);
        assertNotNull(user);
        assertEquals("张三", user.getName());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void fromJsonOrThrow_shouldThrowForNullOrEmptyInput(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> Fastjson2Utils.fromJsonOrThrow(input, User.class));
    }

    @Test
    void fromJsonOrThrow_shouldThrowForInvalidJson() {
        assertThrows(JSONException.class,
                () -> Fastjson2Utils.fromJsonOrThrow("{invalid json}", User.class));
    }

    // Test fromJson with TypeReference
    @Test
    void fromJson_withTypeReference_shouldDeserializeToGenericType() {
        String listJson = "[\"a\", \"b\", \"c\"]";
        List<String> result = Fastjson2Utils.fromJson(listJson, new TypeReference<>() {
        });
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("a", result.getFirst());
    }

    @Test
    void fromJson_withTypeReference_shouldReturnNullForInvalidInput() {
        assertNull(Fastjson2Utils.fromJson(null, new TypeReference<List<String>>() {
        }));
        assertNull(Fastjson2Utils.fromJson("", new TypeReference<List<String>>() {
        }));
        assertNull(Fastjson2Utils.fromJson("invalid json", new TypeReference<List<String>>() {
        }));
    }

    // Test fromJson with Type
    @Test
    void fromJson_withType_shouldDeserializeToType() {
        String mapJson = "{\"key1\":\"value1\",\"key2\":2}";
        Type type = new TypeReference<Map<String, Object>>() {
        }.getType();
        Map<String, Object> result = Fastjson2Utils.fromJson(mapJson, type);

        assertNotNull(result);
        assertEquals("value1", result.get("key1"));
        assertEquals(2, result.get("key2"));
    }

    @Test
    void fromJson_withType_shouldReturnNullForInvalidInput() {
        Type type = new TypeReference<Map<String, String>>() {
        }.getType();
        assertNull(Fastjson2Utils.fromJson(null, type));
        assertNull(Fastjson2Utils.fromJson("", type));
        assertNull(Fastjson2Utils.fromJson("invalid json", type));
    }

    // Test toPrettyJson
    @Test
    void toPrettyJson_shouldReturnFormattedJson() {
        String prettyJson = Fastjson2Utils.toPrettyJson(testUser);
        System.out.println(prettyJson);
        assertNotNull(prettyJson);
        assertTrue(prettyJson.contains("\n"));
        assertTrue(prettyJson.contains("\t")); // Check for indentation
    }

    @Test
    void toPrettyJson_shouldReturnNullForNullInput() {
        assertNull(Fastjson2Utils.toPrettyJson(null));
    }

    // Test isValidJson
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
        assertTrue(Fastjson2Utils.isValidJson(json));
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
        assertFalse(Fastjson2Utils.isValidJson(input));
    }

    @Test
    void isValidJson_shouldReturnFalseForNullInput() {
        assertFalse(Fastjson2Utils.isValidJson(null));
    }

    // Test edge cases
    @Test
    void testEmptyObjectAndArray() {
        // Test empty object
        String emptyObj = "{}";
        assertTrue(Fastjson2Utils.isValidJson(emptyObj));
        assertNotNull(Fastjson2Utils.fromJson(emptyObj, Map.class));
        assertTrue(Fastjson2Utils.fromJson(emptyObj, Map.class).isEmpty());

        // Test empty array
        String emptyArr = "[]";
        assertTrue(Fastjson2Utils.isValidJson(emptyArr));
        assertNotNull(Fastjson2Utils.fromJson(emptyArr, List.class));
        assertTrue(Fastjson2Utils.fromJson(emptyArr, List.class).isEmpty());
    }

    @Test
    void testSpecialCharacters() {
        String specialJson = "{\"name\":\"特殊字符~!@#$%^&*()_+{}|:<>?,./;'[]\\\\-=`\"}";
        assertTrue(Fastjson2Utils.isValidJson(specialJson));
        Map<String, String> result = Fastjson2Utils.fromJson(specialJson, new TypeReference<Map<String, String>>() {
        });
        assertNotNull(result);
        assertEquals("特殊字符~!@#$%^&*()_+{}|:<>?,./;'[]\\-=`", result.get("name"));
    }
}