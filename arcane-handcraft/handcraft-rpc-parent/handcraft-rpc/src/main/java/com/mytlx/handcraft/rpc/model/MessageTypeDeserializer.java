package com.mytlx.handcraft.rpc.model;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ObjectDeserializer;

import java.lang.reflect.Type;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 17:26:44
 */
public class MessageTypeDeserializer implements ObjectDeserializer {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String name = parser.parseObject(String.class);
        return (T) MessageTypeEnum.valueOf(name);
    }
}
