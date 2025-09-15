package com.mytlx.handcraft.rpc.model;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 17:21:30
 */
public class MessageTypeSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        if (object == null) {
            serializer.writeNull();
            return;
        }
        if (object instanceof MessageTypeEnum messageTypeEnum) {
            serializer.write(messageTypeEnum.name());
        }
    }
}
