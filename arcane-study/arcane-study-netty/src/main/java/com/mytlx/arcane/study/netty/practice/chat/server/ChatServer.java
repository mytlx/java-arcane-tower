package com.mytlx.arcane.study.netty.practice.chat.server;

import com.mytlx.arcane.study.netty.practice.chat.message.LoginRequestMessage;
import com.mytlx.arcane.study.netty.practice.chat.message.LoginResponseMessage;
import com.mytlx.arcane.study.netty.practice.chat.protocol.MessageCodecSharable;
import com.mytlx.arcane.study.netty.practice.chat.protocol.ProtocolFrameDecoder;
import com.mytlx.arcane.study.netty.practice.chat.server.service.UserServiceFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
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
                    ch.pipeline().addLast(new SimpleChannelInboundHandler<LoginRequestMessage>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
                            boolean login = UserServiceFactory.getUserService().login(msg.getUsername(), msg.getPassword());
                            LoginResponseMessage resp;
                            if (login) {
                                resp = new LoginResponseMessage(true, "登录成功");
                            } else {
                                resp = new LoginResponseMessage(false, "登录失败");
                            }
                            ctx.writeAndFlush(resp);
                        }
                    });
                }
            });
            Channel channel = bootstrap.bind(8080).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
