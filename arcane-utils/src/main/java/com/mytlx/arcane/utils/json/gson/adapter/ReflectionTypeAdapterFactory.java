package com.mytlx.arcane.utils.json.gson.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

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
            return new ReflectionTypeAdapter<>();
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
            out.name("fieldType").value(field.getType().getName());
            out.name("modifiers").value(field.getModifiers());
            out.endObject();
        }

        @Override
        public T read(JsonReader in) {
            throw new UnsupportedOperationException("Deserialization not supported for reflection types.");
        }
    }
}
