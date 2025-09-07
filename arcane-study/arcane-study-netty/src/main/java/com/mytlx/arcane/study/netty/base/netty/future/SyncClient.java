package com.mytlx.arcane.study.netty.base.netty.future;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-07 12:12:53
 */
@Slf4j
public class SyncClient {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 1. 连接到服务器
                // 异步非阻塞, main 发起了调用，真正执行 connect 是 nio 线程
                .connect("localhost", 8080);

        //========= 2.0 两种同步方式 ==========

        // 2.1 使用 sync 方法同步处理结果
        // channelFuture.sync(); // main 阻塞住当前线程，直到nio线程连接建立完毕
        // Channel channel = channelFuture.channel();
        // log.debug("{}", channel);
        // channel.writeAndFlush("hello, world");

        // 2.2 使用 addListener(回调对象) 方法异步处理结果
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                // 在 nio 线程连接建立好之后，会调用 operationComplete
                Channel channel = future.channel();
                log.debug("{}", channel);
                channel.writeAndFlush("hello, world");
            }
        });
    }
}
