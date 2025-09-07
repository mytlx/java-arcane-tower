package com.mytlx.arcane.study.netty.base.nio.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-05 11:14:45
 */
public class Client {

    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("127.0.0.1", 8080));
        SocketAddress localAddress = sc.getLocalAddress();
        System.out.println("debug breakpoint and send msg");
        sc.close();
    }


}
