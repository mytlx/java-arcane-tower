package com.mytlx.arcane.study.netty.nio.bytebuffer;

import com.mytlx.arcane.study.netty.nio.utils.ByteBufferUtil;

import java.nio.ByteBuffer;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-04 0:08:09
 */
public class Test02ByteBufferReadWrite {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 0x61); // 'a'
        ByteBufferUtil.debugAll(buffer);

        buffer.put(new byte[]{0x62, 0x63, 0x64}); // b  c  d
        ByteBufferUtil.debugAll(buffer);
        // System.out.println(buffer.get());   // 0 会破坏写入位置，position++

        buffer.flip();
        System.out.println(buffer.get());   // a 97
        ByteBufferUtil.debugAll(buffer);

        buffer.compact();
        ByteBufferUtil.debugAll(buffer);

        buffer.put(new byte[]{0x65, 0x6f});
        ByteBufferUtil.debugAll(buffer);
    }
}
