package com.mytlx.arcane.study.netty.practice.chat.server;

import com.mytlx.arcane.study.netty.practice.chat.protocol.MessageCodecSharable;
import com.mytlx.arcane.study.netty.practice.chat.protocol.ProtocolFrameDecoder;
import com.mytlx.arcane.study.netty.practice.chat.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-07 1:41:56
 */
@Slf4j
public class ChatServer {

    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        MessageCodecSharable msgCodec = new MessageCodecSharable();
        LoggingHandler loggingHandler = new LoggingHandler();

        // business handler
        LoginRequestMessageHandler loginRequestMessageHandler = new LoginRequestMessageHandler();
        ChatRequestMessageHandler chatRequestMessageHandler = new ChatRequestMessageHandler();
        GroupCreateRequestMessageHandler groupCreateRequestMessageHandler = new GroupCreateRequestMessageHandler();
        GroupChatRequestMessageHandler groupChatRequestMessageHandler = new GroupChatRequestMessageHandler();
        QuitHandler quitHandler = new QuitHandler();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                            .addLast(new ProtocolFrameDecoder())
                            .addLast(loggingHandler)
                            .addLast(msgCodec);
                    ch.pipeline()
                            .addLast(loginRequestMessageHandler)
                            .addLast(chatRequestMessageHandler)
                            .addLast(groupCreateRequestMessageHandler)
                            .addLast(groupChatRequestMessageHandler)
                            .addLast(quitHandler)
                    ;
                }
            });
            Channel channel = bootstrap.bind(8080).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
