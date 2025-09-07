package com.mytlx.arcane.study.netty.practice.chat.client;

import com.mytlx.arcane.study.netty.practice.chat.message.*;
import com.mytlx.arcane.study.netty.practice.chat.protocol.MessageCodecSharable;
import com.mytlx.arcane.study.netty.practice.chat.protocol.ProtocolFrameDecoder;
import com.mytlx.arcane.study.netty.practice.chat.server.session.SessionFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
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

    public static void main(String[] args) throws Exception {
        System.out.println(Arrays.toString(args));
        if (args.length == 2) {
            start(new ClientConfig.ClientInfo(args[0], args[1]));
        } else {
            start();
        }
    }

    public static void start() {
        start(false, null);
    }

    public static void start(ClientConfig.ClientInfo config) {
        start(true, config);
    }

    public static void start(boolean testFlag, ClientConfig.ClientInfo config) {
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
                            // .addLast(loggingHandler)
                            .addLast(msgCodec);

                    ch.pipeline()
                            // 3s 没有向服务器写数据，会触发一个 IdleState#WRITER_IDLE 事件
                            .addLast(new IdleStateHandler(0, 3, 0))
                            .addLast(new ChannelDuplexHandler() {
                                @Override
                                public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                    IdleStateEvent event = (IdleStateEvent) evt;
                                    if (event.state() == IdleState.WRITER_IDLE) {
                                        // log.debug("3s没有写数据，发送一个心跳包");
                                        ctx.writeAndFlush(new PingMessage());
                                    }
                                }
                            });

                    ch.pipeline().addLast("client handler", new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            // 连接建立后，会触发 active 事件
                            // 负责接收用户在控制台的输入，负责向服务器发送各种消息
                            new Thread(() -> {
                                Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
                                String username = null;
                                String password = null;
                                if (testFlag) {
                                    username = config.getUsername();
                                    password = config.getPassword();
                                    log.debug("使用配置登录，username: {}, password: {}", username, password);
                                } else {
                                    System.out.println("请输入用户名：");
                                    username = scanner.nextLine();
                                    System.out.println("请输入密码：");
                                    password = scanner.nextLine();
                                }
                                // 构造消息对象
                                LoginRequestMessage msg = new LoginRequestMessage(username, password, "");
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
                                    System.out.println("============================================");
                                    System.out.println("    send [username] [content]");
                                    System.out.println("    gsend [group name] [content]");
                                    System.out.println("    gcreate [group name] [m1,m2,m3...]");
                                    System.out.println("    gmembers [group name]");
                                    System.out.println("    gjoin [group name]");
                                    System.out.println("    gquit [group name]");
                                    System.out.println("    quit");
                                    System.out.println("============================================");
                                    String command = null;
                                    try {
                                        command = scanner.nextLine();
                                    } catch (Exception e) {
                                        break;
                                    }
                                    String[] s = command.split(" ");
                                    switch (s[0]) {
                                        case "send":
                                            ctx.writeAndFlush(new ChatRequestMessage(username, s[1], s[2]));
                                            break;
                                        case "gsend":
                                            ctx.writeAndFlush(new GroupChatRequestMessage(username, s[1], s[2]));
                                            break;
                                        case "gcreate":
                                            Set<String> set = new HashSet<>(Arrays.asList(s[2].split(",")));
                                            set.add(username); // 加入自己
                                            ctx.writeAndFlush(new GroupCreateRequestMessage(s[1], set));
                                            break;
                                        case "gmembers":
                                            ctx.writeAndFlush(new GroupMembersRequestMessage(s[1]));
                                            break;
                                        case "gjoin":
                                            ctx.writeAndFlush(new GroupJoinRequestMessage(username, s[1]));
                                            break;
                                        case "gquit":
                                            ctx.writeAndFlush(new GroupQuitRequestMessage(username, s[1]));
                                            break;
                                        case "quit":
                                            ctx.channel().close();
                                            return;
                                    }
                                }

                            }, "system in").start();
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            // log.debug("channel read msg: {}", msg);
                            if (msg instanceof LoginResponseMessage resp) {
                                if (resp.isSuccess()) {
                                    loginFlag.set(true);
                                }
                                latch.countDown();
                            } else if (msg instanceof ChatResponseMessage resp) {
                                if (resp.isSuccess()) {
                                    log.info("收到来自<{}>的消息：{}", resp.getFrom(), resp.getContent());
                                } else {
                                    log.info("发送给<{}>消息失败：{}", resp.getTo(), resp.getMsg());
                                }
                            } else if (msg instanceof GroupCreateResponseMessage resp) {
                                log.info("[系统消息] {}", resp.getMsg());
                            } else if (msg instanceof GroupChatResponseMessage resp) {
                                if (resp.isSuccess()) {
                                    log.info("[{}]<{}>：{}", resp.getGroupName(), resp.getFrom(), resp.getContent());
                                }
                            }
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
