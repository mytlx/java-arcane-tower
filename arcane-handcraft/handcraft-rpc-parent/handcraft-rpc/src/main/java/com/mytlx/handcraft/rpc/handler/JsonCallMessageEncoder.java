package com.mytlx.handcraft.rpc.handler;

import com.alibaba.fastjson2.JSON;
import com.mytlx.handcraft.rpc.model.MessagePayload;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 17:34:18
 */
public class JsonCallMessageEncoder extends MessageToByteEncoder<MessagePayload> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessagePayload msg, ByteBuf byteBuf) throws Exception {

        // 1B：message type
        byteBuf.writeByte(msg.getMessageType().ordinal());

        // 4B: message body length
        byte[] msgBytes = JSON.toJSONBytes(msg);
        byteBuf.writeInt(msgBytes.length);

        // others: message body
        byteBuf.writeBytes(msgBytes, 0, msgBytes.length);

    }
}
