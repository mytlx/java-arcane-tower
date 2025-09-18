package com.mytlx.handcraft.rpc.handler;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.mytlx.handcraft.rpc.utils.KryoThreadLocal;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-18 22:06:18
 */
public class KryoMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 4) {
            return;
        }

        byteBuf.markReaderIndex();
        int length = byteBuf.readInt();

        if (byteBuf.readableBytes() < length) {
            byteBuf.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Input input = new Input(new ByteArrayInputStream(bytes));
        Kryo kryo = KryoThreadLocal.get();
        Object msg = kryo.readClassAndObject(input);

        list.add(msg);
    }
}
