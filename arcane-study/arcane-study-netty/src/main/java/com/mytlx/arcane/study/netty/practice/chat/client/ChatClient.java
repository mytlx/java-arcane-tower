package com.mytlx.arcane.study.netty.practice.chat.client;

import com.mytlx.arcane.study.netty.practice.chat.message.*;
import com.mytlx.arcane.study.netty.practice.chat.protocol.MessageCodecSharable;
import com.mytlx.arcane.study.netty.practice.chat.protocol.ProtocolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-07 1:42:03
 */
@Slf4j
public class ChatClient {

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable msgCodec = new MessageCodecSharable();
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean loginFlag = new AtomicBoolean(false);

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                            .addLast(new ProtocolFrameDecoder())
                            .addLast(loggingHandler)
                            .addLast(msgCodec);
                    ch.pipeline().addLast("client handler", new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            // 连接建立后，会触发 active 事件
                            // 负责接收用户在控制台的输入，负责向服务器发送各种消息
                            new Thread(() -> {
                                Scanner scanner = new Scanner(System.in);
                                System.out.println("请输入用户名：");
                                String username = scanner.nextLine();
                                System.out.println("请输入密码：");
                                String password = scanner.nextLine();
                                // 构造消息对象
                                LoginRequestMessage msg = new LoginRequestMessage(username, password, "张三");
                                // 发送消息
                                ctx.writeAndFlush(msg);

                                try {
                                    latch.await();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }

                                if (!loginFlag.get()) {
                                    ctx.channel().close();
                                    return;
                                }

                                while (true) {
                                    System.out.println("==================================");
                                    System.out.println("send [username] [content]");
                                    System.out.println("gsend [group name] [content]");
                                    System.out.println("gcreate [group name] [m1,m2,m3...]");
                                    System.out.println("gmembers [group name]");
                                    System.out.println("gjoin [group name]");
                                    System.out.println("gquit [group name]");
                                    System.out.println("quit");
                                    System.out.println("==================================");
                                    String command = null;
                                    try {
                                        command = scanner.nextLine();
                                    } catch (Exception e) {
                                        break;
                                    }
                                    String[] s = command.split(" ");
                                    switch (s[0]){
                                        case "send":
                                            break;
                                        case "gsend":
                                            break;
                                        case "gcreate":
                                            break;
                                        case "gmembers":
                                            break;
                                        case "gjoin":
                                            break;
                                        case "gquit":
                                            break;
                                        case "quit":
                                            ctx.channel().close();
                                            return;
                                    }
                                }

                            }, "system in").start();

                            super.channelActive(ctx);
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            log.debug("channel read msg: {}", msg);
                            if (msg instanceof LoginResponseMessage resp) {
                                if (resp.isSuccess()) {
                                    loginFlag.set(true);
                                }
                                latch.countDown();
                            }

                            super.channelRead(ctx, msg);
                        }
                    });
                }
            });
            Channel channel = bootstrap.connect("localhost", 8080).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
