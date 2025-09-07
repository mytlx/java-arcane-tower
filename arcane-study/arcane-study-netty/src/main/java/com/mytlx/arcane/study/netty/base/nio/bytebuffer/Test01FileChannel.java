package com.mytlx.arcane.study.netty.base.nio.bytebuffer;

import com.mytlx.arcane.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用 FileChannel 和 ByteBuffer 来读取文件内容
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-03 22:34:59
 */
@Slf4j
public class Test01FileChannel {

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(FileUtils.getResourceFile("data01.txt").getAbsolutePath());

        try (RandomAccessFile file = new RandomAccessFile(FileUtils.getResourceFile("data01.txt"), "rw")) {
            FileChannel channel = file.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
            while (true) {
                // 从channel读取，写入buffer
                int len = channel.read(byteBuffer);
                log.debug("读到字节数：{}", len);
                if (len == -1) {
                    break;
                }
                // 切换成 读模式
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    log.debug("{}", (char) byteBuffer.get());
                }
                // 切换成 写模式
                byteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
