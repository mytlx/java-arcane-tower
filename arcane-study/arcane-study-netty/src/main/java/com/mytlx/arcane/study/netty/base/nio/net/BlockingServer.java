package com.mytlx.arcane.study.netty.base.nio.net;

import com.mytlx.arcane.study.netty.base.nio.utils.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * 单线程 bio server
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-05 11:02:05
 */
@Slf4j
public class BlockingServer {

    public static void main(String[] args) throws IOException {
        // 1. ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(16);
        // 2. 创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 3. 绑定监听端口
        ssc.bind(new InetSocketAddress(8080));

        // 4. 连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
            // 5. accept 建立与客户端连接， SocketChannel 用来与客户端之间通信
            log.debug("connecting...");
            SocketChannel sc = ssc.accept();    // 阻塞方法，线程停止运行
            channels.add(sc);
            log.debug("connected... {}", sc);

            for (SocketChannel channel : channels) {
                // 6. 接收客户端发送的数据
                log.debug("before read... {}", channel);
                int read = channel.read(byteBuffer);    // 阻塞方法，线程停止运行
                byteBuffer.flip();
                ByteBufferUtil.debugRead(byteBuffer);
                byteBuffer.clear();
                log.debug("after read...{}", channel);
            }
        }

    }


}
