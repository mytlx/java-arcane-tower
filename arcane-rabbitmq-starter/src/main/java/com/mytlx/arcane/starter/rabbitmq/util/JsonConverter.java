package com.mytlx.arcane.starter.rabbitmq.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serial;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-10-31 13:23:32
 */
@Component
@Slf4j
public class JsonConverter {
    private final ObjectMapper objectMapper;

    public JsonConverter(ObjectMapper objectMapper) {
        // 注入 Spring Boot 全局 ObjectMapper
        this.objectMapper = objectMapper;
    }

    /**
     * 将对象转换为 JSON 字符串
     *
     * @param data 任意对象
     * @return JSON 字符串
     */
    public String toJson(Object data) {
        if (data == null) {
            return null;
        }
        try {
            log.debug("Converting object to json: {}", data);
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("JSON 序列化失败，对象类型：{}，内容：{}",
                    data.getClass().getName(), data, e);
            throw new JsonConvertException("JSON 序列化失败: " + data.getClass().getName(), e);
        }
    }

    /**
     * 自定义异常，专用于 JSON 转换失败
     */
    public static class JsonConvertException extends RuntimeException {
        @Serial
        private static final long serialVersionUID = 1L;

        public JsonConvertException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
