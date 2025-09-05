package com.mytlx.arcane.study.netty.nio.bytebuffer;

import com.mytlx.arcane.study.netty.nio.utils.ByteBufferUtil;
import com.mytlx.arcane.utils.FileUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-04 0:25:44
 */
public class Test06ScatteringReads {
    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile(
                FileUtils.getResourceFile("scatterData.txt"), "r");
             FileChannel channel = file.getChannel()) {
            ByteBuffer b1 = ByteBuffer.allocate(3);
            ByteBuffer b2 = ByteBuffer.allocate(3);
            ByteBuffer b3 = ByteBuffer.allocate(5);
            channel.read(new ByteBuffer[]{b1, b2, b3});
            b1.flip();
            b2.flip();
            b3.flip();
            ByteBufferUtil.debugAll(b1);
            ByteBufferUtil.debugAll(b2);
            ByteBufferUtil.debugAll(b3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
