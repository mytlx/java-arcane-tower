package com.mytlx.arcane.utils.json.gson.adapter;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 反射类型适配器
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-15 15:38
 */
public class ReflectionTypeAdapter<T> extends TypeAdapter<T> {

    @Override
    public void write(JsonWriter out, T value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        if (value instanceof Constructor) {
            writeConstructor(out, (Constructor<?>) value);
        } else if (value instanceof Method) {
            writeMethod(out, (Method) value);
        } else if (value instanceof Field) {
            writeField(out, (Field) value);
        } else if (value instanceof Class) {
            writeClass(out, (Class<?>) value);
        } else {
            out.beginObject();
            out.name("type").value(value.getClass().getName());
            out.name("value").value(value.toString());
            out.endObject();
        }
    }

    private void writeConstructor(JsonWriter out, Constructor<?> constructor) throws IOException {
        out.beginObject();
        out.name("type").value("Constructor");
        out.name("name").value(constructor.getName());
        out.name("parameterTypes").beginArray();
        for (Parameter parameter : constructor.getParameters()) {
            out.value(parameter.getType().getName());  // 序列化参数类型
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
        for (Parameter parameter : method.getParameters()) {
            out.value(parameter.getType().getName());  // 序列化参数类型
        }
        out.endArray();
        out.name("modifiers").value(method.getModifiers());
        out.endObject();
    }

    private void writeField(JsonWriter out, Field field) throws IOException {
        out.beginObject();
        out.name("type").value("Field");
        out.name("name").value(field.getName());
        out.name("type").value(field.getType().getName());
        out.name("modifiers").value(field.getModifiers());
        out.endObject();
    }

    private void writeClass(JsonWriter out, Class<?> clazz) throws IOException {
        out.beginObject();
        out.name("type").value("Class");
        out.name("name").value(clazz.getName());
        out.name("modifiers").value(clazz.getModifiers());
        out.endObject();
    }

    @Override
    public T read(JsonReader in) throws IOException {
        throw new UnsupportedOperationException("Deserialization not supported for reflection types.");
    }
}

