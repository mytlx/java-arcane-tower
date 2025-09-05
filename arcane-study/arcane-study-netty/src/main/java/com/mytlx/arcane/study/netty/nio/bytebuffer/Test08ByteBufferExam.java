package com.mytlx.arcane.study.netty.nio.bytebuffer;

import com.mytlx.arcane.study.netty.nio.utils.ByteBufferUtil;

import java.nio.ByteBuffer;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-04 0:47:14
 */
public class Test08ByteBufferExam {
    public static void main(String[] args) {
         /*
             网络上有多条数据发送给服务端，数据之间使用 \n 进行分隔
             但由于某种原因这些数据在接收时，被进行了重新组合，例如原始数据有3条为
                 Hello,world\n
                 I'm zhangsan\n
                 How are you?\n
             变成了下面的两个 byteBuffer (黏包，半包)
                 Hello,world\nI'm zhangsan\nHo
                 w are you?\n
             现在要求你编写程序，将错乱的数据恢复成原始的按 \n 分隔的数据
         */
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\n".getBytes());
        split(source);
    }

    private static void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            if (source.get(i) == '\n') {
                // 0 1  2 3 4 5
                // a \n b c d \n
                // i == 5, position == 2
                // 需要 \n 就 +1，不需要不用加
                int len = i + 1 - source.position();
                ByteBuffer target = ByteBuffer.allocate(len);
                for (int j = 0; j < len; j++) {
                    target.put(source.get());
                }
                ByteBufferUtil.debugAll(target);
            }
        }
        source.compact();
    }
}
