package com.mytlx.arcane.study.netty.practice.chat.protocol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mytlx.arcane.study.netty.practice.chat.message.Message;
import com.mytlx.arcane.utils.YamlUtils;
import com.mytlx.arcane.utils.json.gson.GsonUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 用于扩展序列化、反序列化算法
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-07 21:20:40
 */
public interface Serializer {

    <T> T deserialize(Class<T> clazz,byte[] bytes);

    <T> byte[] serialize(T object);

    @SuppressWarnings("unchecked")
    enum Algorithm implements Serializer {
        JDK(0) {
            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {
                try {
                    return (T) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("反序列化失败", e);
                }
            }

            @Override
            public <T> byte[] serialize(T object) {
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(object);
                    return bos.toByteArray();
                } catch (IOException e) {
                    throw new RuntimeException("序列化失败", e);
                }
            }
        },

        GSON(1) {

            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {
                return GsonUtils.fromJson(new String(bytes, StandardCharsets.UTF_8), clazz);
            }

            @Override
            public <T> byte[] serialize(T object) {
                return GsonUtils.toJson(object).getBytes(StandardCharsets.UTF_8);
            }
        },
        ;

        private final int code;

        Algorithm(int code) {
            this.code = code;
        }

        public static Algorithm getFromConfig() {
            return Serializer.Algorithm.valueOf(YamlUtils.getString("serializer.algorithm"));
        }

        public static int getCodeFromConfig() {
            return getFromConfig().code;
        }

        public static Algorithm getFromCode(int code) {
            for (Algorithm algorithm : values()) {
                if (algorithm.code == code) {
                    return algorithm;
                }
            }
            throw new RuntimeException("未找到对应的序列化算法，code: " + code);
        }
    }
}
