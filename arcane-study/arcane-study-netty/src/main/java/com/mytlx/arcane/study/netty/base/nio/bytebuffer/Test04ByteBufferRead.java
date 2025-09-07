package com.mytlx.arcane.study.netty.base.nio.bytebuffer;

import java.nio.ByteBuffer;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-04 0:19:50
 */
public class Test04ByteBufferRead {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        buffer.flip();

        // rewind 从头开始读
        // buffer.get(new byte[4]);
        // ByteBufferUtil.debugAll(buffer);
        // buffer.rewind();
        // System.out.println((char)buffer.get());

        // mark & reset
        // mark 做一个标记，记录 position 位置， reset 是将 position 重置到 mark 的位置
        // System.out.println((char) buffer.get());
        // System.out.println((char) buffer.get());
        // buffer.mark(); // 加标记，索引2 的位置
        // System.out.println((char) buffer.get());
        // System.out.println((char) buffer.get());
        // buffer.reset(); // 将 position 重置到索引 2
        // System.out.println((char) buffer.get());
        // System.out.println((char) buffer.get());

        // get(i) 不会改变读索引的位置
        // System.out.println((char) buffer.get(3));
        // ByteBufferUtil.debugAll(buffer);
    }
}
