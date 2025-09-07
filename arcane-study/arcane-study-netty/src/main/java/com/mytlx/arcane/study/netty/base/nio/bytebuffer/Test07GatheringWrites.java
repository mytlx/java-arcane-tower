package com.mytlx.arcane.study.netty.base.nio.bytebuffer;

import com.mytlx.arcane.utils.FileUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-04 0:33:46
 */
public class Test07GatheringWrites {
    public static void main(String[] args) {
        ByteBuffer b1 = StandardCharsets.UTF_8.encode("hello");
        ByteBuffer b2 = StandardCharsets.UTF_8.encode("world");
        ByteBuffer b3 = StandardCharsets.UTF_8.encode("你好");


        try (RandomAccessFile file = new RandomAccessFile(
                FileUtils.getResourceFile("gatheringData.txt"), "rw");
             FileChannel channel = file.getChannel()) {

            while (true) {
                long write = channel.write(new ByteBuffer[]{b1, b2, b3});
                if (write != 0) {
                    break;
                }
                System.out.println("写入完毕");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
