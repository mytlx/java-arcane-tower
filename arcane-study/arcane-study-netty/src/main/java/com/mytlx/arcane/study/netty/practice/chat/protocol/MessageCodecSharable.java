package com.mytlx.arcane.study.netty.practice.chat.protocol;

import com.mytlx.arcane.study.netty.practice.chat.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-07 1:39:15
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> list) throws Exception {
        ByteBuf buf = ctx.alloc().buffer();
        // 4 字节的魔数
        buf.writeBytes(new byte[]{'c', 'h', 'a', 't' });
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

        list.add(buf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        int magicNum = buf.readInt();
        byte version = buf.readByte();
        byte serializeAlgorithm = buf.readByte();
        byte msgType = buf.readByte();
        int sequenceId = buf.readInt();
        buf.readByte();
        int length = buf.readInt();
        byte[] contentBytes = new byte[length];
        buf.readBytes(contentBytes, 0, length);

        // 找到对应的反序列化算法
        Serializer.Algorithm serializer = Serializer.Algorithm.getFromCode(serializeAlgorithm);
        // 确定具体消息类型
        Class<?> messageClass = Message.getMessageClass(msgType);
        Object message = serializer.deserialize(messageClass, contentBytes);

        // log.debug("magicNum: {}, version: {}, serializeType: {}, msgType: {}, sequenceId: {}, length: {}",
        //         magicNum, version, serializeAlgorithm, msgType, sequenceId, length);
        // log.debug("message: {}", message);

        list.add(message);
    }
}
