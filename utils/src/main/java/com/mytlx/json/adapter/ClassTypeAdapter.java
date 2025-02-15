package com.mytlx.json.adapter;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-15 15:31
 */
public class ClassTypeAdapter extends TypeAdapter<Class<?>> {

    @Override
    public void write(JsonWriter out, Class<?> value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.getName()); // 序列化为类的名字
        }
    }

    @Override
    public Class<?> read(JsonReader in) throws IOException {
        try {
            return Class.forName(in.nextString()); // 反序列化为 Class 对象
        } catch (ClassNotFoundException e) {
            throw new IOException("Class not found", e);
        }
    }
}
