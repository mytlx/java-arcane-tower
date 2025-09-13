package com.mytlx.arcane.study.netty.base.netty.parameter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-07 23:49:04
 */
public class Test02BacklogServer {
    public static void main(String[] args) {
        // 需要断点 io.netty.channel.nio.NioEventLoop#processSelectedKey
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .option(ChannelOption.SO_BACKLOG, 2) // 全队列满了
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new LoggingHandler());
                    }
                }).bind(8080);
    }
}
