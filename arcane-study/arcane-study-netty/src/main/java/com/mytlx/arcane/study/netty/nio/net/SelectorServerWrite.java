package com.mytlx.arcane.study.netty.nio.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-05 12:37:27
 */
public class SelectorServerWrite {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8080));
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    SelectionKey register = sc.register(selector, SelectionKey.OP_READ);

                    // 1. 向客户端发送内容
                    ByteBuffer buffer = Charset.defaultCharset().encode("a".repeat(30000000));
                    // 2. write 表示实际写了多少字节
                    int write = sc.write(buffer);
                    System.out.println("实际写入字节 = " + write);
                    // 3. 如果有剩余未读字节，才需要关注写事件
                    if (buffer.hasRemaining()) {
                        // read 1  write 4
                        // 4. 在原有关注事件的基础上，多关注 写事件
                        register.interestOps(register.interestOps() | SelectionKey.OP_WRITE);
                        // 5. 把未写完的 buffer 作为附件加入 key
                        register.attach(buffer);
                    }
                } else if (key.isWritable()) {
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    SocketChannel sc = (SocketChannel) key.channel();
                    int write = sc.write(buffer);
                    System.out.println("实际写入字节:" + write);
                    if (!buffer.hasRemaining()) {
                        // 清除写事件
                        // 如果用减法的话，一旦原事件中没有写事件，会导致错乱
                        // 但是用按位与非就不会出现这个问题
                        key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
                        // 清除buffer
                        key.attach(null);
                    }
                }
            }
        }
    }


}
