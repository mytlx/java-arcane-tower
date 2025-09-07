package com.mytlx.arcane.study.netty.practice.chat;

import com.mytlx.arcane.study.netty.practice.chat.message.LoginRequestMessage;
import com.mytlx.arcane.study.netty.practice.chat.message.Message;
import com.mytlx.arcane.study.netty.practice.chat.protocol.MessageCodecSharable;
import com.mytlx.arcane.study.netty.practice.chat.protocol.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.jupiter.api.Test;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-07 22:59:17
 */
public class SerializerTest {

    @Test
    public void test() throws Exception {
        MessageCodecSharable CODEC = new MessageCodecSharable();
        LoggingHandler LOGGING = new LoggingHandler();
        EmbeddedChannel channel = new EmbeddedChannel(LOGGING, CODEC, LOGGING);

        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
        channel.writeOutbound(message);

        ByteBuf buf = msgToByteBuf(message);
        channel.writeInbound(buf);
    }

    public ByteBuf msgToByteBuf(Message msg) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        // 4 字节的魔数
        buf.writeBytes(new byte[]{'c', 'h', 'a', 't'});
        // 1 字节的版本号
        buf.writeByte(1);
        // 1 字节的序列化方式，enum Serializer.Algorithm
        buf.writeByte(Serializer.Algorithm.getCodeFromConfig());
        // 1 字节的指令类型
        buf.writeByte(msg.getMessageType());
        // 4 字节的序列号
        buf.writeInt(msg.getSequenceId());
        // 无意义，对齐填充
        buf.writeByte(0xff);
        // 获取内容的字节数组
        byte[] msgBytes = Serializer.Algorithm.getFromConfig().serialize(msg);
        // 4 字节的长度
        buf.writeInt(msgBytes.length);
        // 内容
        buf.writeBytes(msgBytes);

        return buf;
    }

}