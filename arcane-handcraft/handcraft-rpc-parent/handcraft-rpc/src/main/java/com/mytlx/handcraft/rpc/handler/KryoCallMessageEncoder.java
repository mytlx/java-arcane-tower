package com.mytlx.handcraft.rpc.handler;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.mytlx.handcraft.rpc.model.MessagePayload;
import com.mytlx.handcraft.rpc.utils.KryoThreadLocal;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-18 22:03:29
 */
public class KryoCallMessageEncoder extends MessageToByteEncoder<MessagePayload> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessagePayload messagePayload, ByteBuf byteBuf) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        Kryo kryo = KryoThreadLocal.get();
        kryo.writeClassAndObject(output, messagePayload);
        output.close();

        byte[] byteArray = baos.toByteArray();
        byteBuf.writeInt(byteArray.length);
        byteBuf.writeBytes(byteArray);

        KryoThreadLocal.remove();
    }
}
