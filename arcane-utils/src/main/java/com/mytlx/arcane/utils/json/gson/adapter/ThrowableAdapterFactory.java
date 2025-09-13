package com.mytlx.arcane.utils.json.gson.adapter;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Constructor;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 13:35:05
 */
public class ThrowableAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        // Only handles Throwable and subclasses; let other factories handle any other type
        if (!Throwable.class.isAssignableFrom(type.getRawType())) {
            return null;
        }

        @SuppressWarnings("unchecked")
        TypeAdapter<T> adapter = (TypeAdapter<T>) new TypeAdapter<Throwable>() {
            @Override
            public void write(JsonWriter out, Throwable t) throws IOException {
                if (t == null) {
                    out.nullValue();
                    return;
                }

                out.beginObject();
                // Include exception type name to give more context; for example NullPointerException might
                // not have a message
                out.name("type");
                out.value(t.getClass().getName());

                out.name("message");
                out.value(t.getMessage());

                Throwable cause = t.getCause();
                if (cause != null && cause != t) {
                    out.name("cause");
                    write(out, cause);
                }

                Throwable[] suppressedArray = t.getSuppressed();
                if (suppressedArray.length > 0) {
                    out.name("suppressed");
                    out.beginArray();

                    for (Throwable suppressed : suppressedArray) {
                        write(out, suppressed);
                    }

                    out.endArray();
                }

                // stackTrace
                out.name("stackTrace").beginArray();
                for (StackTraceElement e : t.getStackTrace()) {
                    out.value(e.toString());
                }
                out.endArray();

                out.endObject();
            }

            @Override
            public Throwable read(JsonReader in) throws IOException {
                JsonElement element = JsonParser.parseReader(in);
                if (element.isJsonNull()) return null;
                JsonObject jsonObject = element.getAsJsonObject();
                String className = jsonObject.get("type").getAsString();  // 序列化时保存的实际类型
                String message = jsonObject.get("message").getAsString();

                try {
                    Class<?> clazz = Class.forName(className);
                    Constructor<?> constructor = clazz.getConstructor(String.class);
                    return (Throwable) constructor.newInstance(message);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to deserialize throwable type", e);
                }

            }
        };
        return adapter;
    }
}
