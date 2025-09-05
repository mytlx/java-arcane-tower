package com.mytlx.arcane.study.netty.nio.file;

import com.mytlx.arcane.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-04 0:58:22
 */
public class Test01FileChannelTransferTo {
    public static void main(String[] args) {
        File from = FileUtils.getResourceFile("from.txt");
        File to = FileUtils.getResourceFile("to.txt");
        try (
                FileInputStream fromFile = new FileInputStream(from);
                FileChannel fromChannel = fromFile.getChannel();
                FileOutputStream toFile = new FileOutputStream(to);
                FileChannel toChannel = toFile.getChannel();
        ) {
            // 效率高，底层会利用操作系统的零拷贝进行优化, 2g 数据
            long size = fromChannel.size();
            // left 变量代表还剩余多少字节
            for (long left = size; left > 0; ) {
                System.out.println("position:" + (size - left) + " left:" + left);
                left -= fromChannel.transferTo((size - left), left, toChannel);
            }
            System.out.println("complete");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
