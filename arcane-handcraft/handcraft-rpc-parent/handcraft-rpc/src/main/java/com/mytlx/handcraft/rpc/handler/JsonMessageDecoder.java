package com.mytlx.handcraft.rpc.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.util.TypeUtils;
import com.mytlx.handcraft.rpc.model.MessagePayload;
import com.mytlx.handcraft.rpc.model.MessageTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 17:47:08
 */
public class JsonMessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 5) return;

        // 记录指针位置
        byteBuf.markReaderIndex();

        byte messageType = byteBuf.readByte();
        int length = byteBuf.readInt();

        if (byteBuf.readableBytes() < length) {
            // 指针复位
            byteBuf.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        MessagePayload messagePayload = JSON.parseObject(bytes, MessagePayload.class);
        JSONObject payload = (JSONObject) messagePayload.getPayload();

        switch (MessageTypeEnum.getByCode(messageType)) {
            case REGISTER, HEARTBEAT -> messagePayload.setPayload(null);
            case CALL, FORWARD -> {
                MessagePayload.RpcRequest req = payload.toJavaObject(MessagePayload.RpcRequest.class);

                // 类型转换，根据 paramTypes 还原 params 类型
                String[] paramTypeNames = req.getParameterTypes();
                Object[] rawParams = req.getParameters();
                Object[] castParams = new Object[rawParams.length];
                for (int i = 0; i < rawParams.length; i++) {
                    Class<?> targetType;
                    try {
                        targetType = Class.forName(paramTypeNames[i]);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("找不到参数类型: " + paramTypeNames[i], e);
                    }
                    castParams[i] = TypeUtils.cast(rawParams[i], targetType);
                }
                req.setParameters(castParams);

                messagePayload.setPayload(req);
            }
            case RESPONSE -> messagePayload.setPayload(payload.toJavaObject(MessagePayload.RpcResponse.class));
            case UNKNOWN -> throw new IllegalArgumentException("未知消息类型: " + messageType);
        }

        list.add(messagePayload);
    }
}
