package com.mytlx.arcane.utils.json.gson.adapter;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * 反射、反射数组
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 11:58:52
 */
public class ReflectionTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<?> rawType = type.getRawType();

        // 单个类型或数组类型
        if (Constructor.class.isAssignableFrom(rawType)
                || Method.class.isAssignableFrom(rawType)
                || Field.class.isAssignableFrom(rawType)
                || Class.class.isAssignableFrom(rawType)
                || (rawType.isArray() && (
                Constructor.class.isAssignableFrom(rawType.getComponentType()) ||
                        Method.class.isAssignableFrom(rawType.getComponentType()) ||
                        Field.class.isAssignableFrom(rawType.getComponentType()) ||
                        Class.class.isAssignableFrom(rawType.getComponentType())
        ))) {
            return new ReflectionTypeAdapter<>(type);
        }
        return null;
    }

    /**
     * 反射类型适配器
     *
     * @author TLX
     * @version 1.0.0
     * @since 2025-02-15 15:38
     */
    public static class ReflectionTypeAdapter<T> extends TypeAdapter<T> {

        private final TypeToken<T> typeToken;

        public ReflectionTypeAdapter(TypeToken<T> typeToken) {
            this.typeToken = typeToken;
        }

        private static final Map<String, Class<?>> PRIMITIVE_MAP = new HashMap<>();

        static {
            PRIMITIVE_MAP.put("byte", byte.class);
            PRIMITIVE_MAP.put("short", short.class);
            PRIMITIVE_MAP.put("int", int.class);
            PRIMITIVE_MAP.put("long", long.class);
            PRIMITIVE_MAP.put("float", float.class);
            PRIMITIVE_MAP.put("double", double.class);
            PRIMITIVE_MAP.put("boolean", boolean.class);
            PRIMITIVE_MAP.put("char", char.class);
        }

        @Override
        public void write(JsonWriter out, T value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }

            Class<?> clazz = value.getClass();

            if (value instanceof Constructor) {
                writeConstructor(out, (Constructor<?>) value);
            } else if (value instanceof Method) {
                writeMethod(out, (Method) value);
            } else if (value instanceof Field) {
                writeField(out, (Field) value);
            } else if (value instanceof Class) {
                writeClass(out, (Class<?>) value);
            } else if (clazz.isArray()) {
                Class<?> componentType = clazz.getComponentType();
                if (Constructor.class.isAssignableFrom(componentType)) {
                    writeConstructorArray(out, (Constructor<?>[]) value);
                } else if (Method.class.isAssignableFrom(componentType)) {
                    writeMethodArray(out, (Method[]) value);
                } else if (Field.class.isAssignableFrom(componentType)) {
                    writeFieldArray(out, (Field[]) value);
                } else if (Class.class.isAssignableFrom(componentType)) {
                    writeClassArray(out, (Class<?>[]) value);
                } else {
                    // 其他数组类型默认处理
                    out.beginArray();
                    Object[] arr = (Object[]) value;
                    for (Object o : arr) {
                        out.value(o.toString());
                    }
                    out.endArray();
                }
            } else {
                out.beginObject();
                out.name("type").value(value.getClass().getName());
                out.name("value").value(value.toString());
                out.endObject();
            }
        }

        private void writeClassArray(JsonWriter out, Class<?>[] arr) throws IOException {
            out.beginArray();
            for (Class<?> c : arr) {
                writeClass(out, c);
            }
            out.endArray();
        }

        private void writeConstructorArray(JsonWriter out, Constructor<?>[] arr) throws IOException {
            out.beginArray();
            for (Constructor<?> c : arr) {
                writeConstructor(out, c);
            }
            out.endArray();
        }

        private void writeMethodArray(JsonWriter out, Method[] arr) throws IOException {
            out.beginArray();
            for (Method m : arr) {
                writeMethod(out, m);
            }
            out.endArray();
        }

        private void writeFieldArray(JsonWriter out, Field[] arr) throws IOException {
            out.beginArray();
            for (Field f : arr) {
                writeField(out, f);
            }
            out.endArray();
        }

        private void writeClass(JsonWriter out, Class<?> clazz) throws IOException {
            out.beginObject();
            out.name("type").value("Class");
            out.name("name").value(clazz.getName());
            out.name("modifiers").value(clazz.getModifiers());
            out.endObject();
        }

        private void writeConstructor(JsonWriter out, Constructor<?> constructor) throws IOException {
            out.beginObject();
            out.name("type").value("Constructor");
            out.name("name").value(constructor.getName());
            out.name("parameterTypes").beginArray();
            for (Parameter p : constructor.getParameters()) {
                out.value(p.getType().getName());
            }
            out.endArray();
            out.name("modifiers").value(constructor.getModifiers());
            out.endObject();
        }

        private void writeMethod(JsonWriter out, Method method) throws IOException {
            out.beginObject();
            out.name("className").value(method.getDeclaringClass().getName());
            out.name("type").value("Method");
            out.name("name").value(method.getName());
            out.name("returnType").value(method.getReturnType().getName());
            out.name("parameterTypes").beginArray();
            for (Parameter p : method.getParameters()) {
                out.value(p.getType().getName());
            }
            out.endArray();
            out.name("modifiers").value(method.getModifiers());
            out.endObject();
        }

        private void writeField(JsonWriter out, Field field) throws IOException {
            out.beginObject();
            out.name("type").value("Field");
            out.name("name").value(field.getName());
            out.name("declaringClass").value(field.getDeclaringClass().getName());
            out.name("fieldType").value(field.getType().getName());
            out.name("modifiers").value(field.getModifiers());
            out.endObject();
        }

        @SuppressWarnings("unchecked")
        @Override
        public T read(JsonReader in) {
            JsonElement element = JsonParser.parseReader(in);
            if (element.isJsonNull()) return null;

            Class<?> clazz = (Class<?>) typeToken.getRawType();

            try {
                if (clazz.isArray()) {
                    Class<?> componentType = clazz.getComponentType();
                    if (Class.class.isAssignableFrom(componentType)) {
                        return (T) readClassArray(element.getAsJsonArray());
                    } else if (Constructor.class.isAssignableFrom(componentType)) {
                        return (T) readConstructorArray(element.getAsJsonArray());
                    } else if (Method.class.isAssignableFrom(componentType)) {
                        return (T) readMethodArray(element.getAsJsonArray());
                    } else if (Field.class.isAssignableFrom(componentType)) {
                        return (T) readFieldArray(element.getAsJsonArray());
                    }
                } else {
                    if (Class.class.isAssignableFrom(clazz)) {
                        return (T) readClass(element.getAsJsonObject());
                    } else if (Constructor.class.isAssignableFrom(clazz)) {
                        return (T) readConstructor(element.getAsJsonObject());
                    } else if (Method.class.isAssignableFrom(clazz)) {
                        return (T) readMethod(element.getAsJsonObject());
                    } else if (Field.class.isAssignableFrom(clazz)) {
                        return (T) readField(element.getAsJsonObject());
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to deserialize reflection type", e);
            }

            throw new UnsupportedOperationException("Unsupported type: " + clazz.getName());
        }

        // --- 单个对象 ---
        private Class<?> readClass(JsonObject obj) throws ClassNotFoundException {
            String className = obj.get("name").getAsString();
            return Class.forName(className);
        }

        private Constructor<?> readConstructor(JsonObject obj) throws Exception {
            String className = obj.get("name").getAsString();
            JsonArray paramTypes = obj.getAsJsonArray("parameterTypes");
            Class<?> clazz = Class.forName(className);
            Class<?>[] params = new Class<?>[paramTypes.size()];
            for (int i = 0; i < paramTypes.size(); i++) {
                String typeName = paramTypes.get(i).getAsString();
                if (PRIMITIVE_MAP.containsKey(typeName)) {
                    params[i] = PRIMITIVE_MAP.get(typeName);  // 基础类型
                } else {
                    params[i] = Class.forName(typeName);      // 引用类型
                }
            }
            return clazz.getConstructor(params);
        }

        private Method readMethod(JsonObject obj) throws Exception {
            String className = obj.get("className").getAsString();
            String methodName = obj.get("name").getAsString();
            JsonArray paramTypes = obj.getAsJsonArray("parameterTypes");
            Class<?> clazz = Class.forName(className);
            Class<?>[] params = new Class<?>[paramTypes.size()];
            for (int i = 0; i < paramTypes.size(); i++) {
                String typeName = paramTypes.get(i).getAsString();
                if (PRIMITIVE_MAP.containsKey(typeName)) {
                    params[i] = PRIMITIVE_MAP.get(typeName);  // 基础类型
                } else {
                    params[i] = Class.forName(typeName);      // 引用类型
                }
            }
            return clazz.getMethod(methodName, params);
        }

        private Field readField(JsonObject obj) throws Exception {
            String className = obj.get("declaringClass").getAsString();
            String fieldName = obj.get("name").getAsString();
            Class<?> clazz = Class.forName(className);
            return clazz.getDeclaredField(fieldName);
        }

        // --- 数组 ---
        private Class<?>[] readClassArray(JsonArray arr) throws ClassNotFoundException {
            Class<?>[] result = new Class[arr.size()];
            for (int i = 0; i < arr.size(); i++) {
                result[i] = readClass(arr.get(i).getAsJsonObject());
            }
            return result;
        }

        private Constructor<?>[] readConstructorArray(JsonArray arr) throws Exception {
            Constructor<?>[] result = new Constructor[arr.size()];
            for (int i = 0; i < arr.size(); i++) {
                result[i] = readConstructor(arr.get(i).getAsJsonObject());
            }
            return result;
        }

        private Method[] readMethodArray(JsonArray arr) throws Exception {
            Method[] result = new Method[arr.size()];
            for (int i = 0; i < arr.size(); i++) {
                result[i] = readMethod(arr.get(i).getAsJsonObject());
            }
            return result;
        }

        private Field[] readFieldArray(JsonArray arr) throws Exception {
            Field[] result = new Field[arr.size()];
            for (int i = 0; i < arr.size(); i++) {
                result[i] = readField(arr.get(i).getAsJsonObject());
            }
            return result;
        }
    }

}
