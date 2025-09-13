package com.mytlx.handcraft.rpc.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mytlx.handcraft.rpc.model.MessagePayload;
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
        // tlxTODO: messageType enum
        switch (messageType) {
            case 1, 5:
                messagePayload.setPayload(null);
                break;
            case 2, 3:
                MessagePayload.RpcRequest request = payload.toJavaObject(MessagePayload.RpcRequest.class);
                messagePayload.setPayload(request);
                break;
            case 4:
                MessagePayload.RpcResponse response = payload.toJavaObject(MessagePayload.RpcResponse.class);
                messagePayload.setPayload(response);
                break;
        }

        list.add(messagePayload);
    }
}
