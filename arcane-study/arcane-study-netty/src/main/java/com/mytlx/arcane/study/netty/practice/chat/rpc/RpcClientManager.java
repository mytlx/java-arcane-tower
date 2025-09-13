package com.mytlx.arcane.study.netty.practice.chat.rpc;

import com.mytlx.arcane.study.netty.practice.chat.message.RpcRequestMessage;
import com.mytlx.arcane.study.netty.practice.chat.protocol.MessageCodecSharable;
import com.mytlx.arcane.study.netty.practice.chat.protocol.ProtocolFrameDecoder;
import com.mytlx.arcane.study.netty.practice.chat.protocol.SequenceIdGenerator;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 15:07:47
 */
@Slf4j
public class RpcClientManager {

    public static void main(String[] args) {
        HelloService helloService = getProxyService(HelloService.class);
        helloService.sayHello("zhangsan");
        helloService.sayHello("lisi");
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProxyService(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                (proxy, method, args) -> {
                    // 1. 将方法调用转换为消息对象
                    RpcRequestMessage rpcRequestMessage = new RpcRequestMessage(
                            SequenceIdGenerator.next(),
                            clazz.getName(),
                            method.getName(),
                            method.getReturnType(),
                            method.getParameterTypes(),
                            args
                    );
                    // 2. 将消息对象发送出去
                    getChannel().writeAndFlush(rpcRequestMessage);
                    return null;
                }
        );
    }

    private static Channel channel;
    public static final Object LOCK = new Object();

    public static Channel getChannel() {
        if (channel != null) return channel;

        synchronized (LOCK) {
            if (channel != null) return channel;
            initChannel();
            return channel;
        }
    }

    private static void initChannel() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        RpcResponseMessageHandler RPC_HANDLER = new RpcResponseMessageHandler();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProtocolFrameDecoder());
                ch.pipeline().addLast(LOGGING_HANDLER);
                ch.pipeline().addLast(MESSAGE_CODEC);
                ch.pipeline().addLast(RPC_HANDLER);
            }
        });

        try {
            channel = bootstrap.connect("localhost", 8080).sync().channel();
            channel.closeFuture().addListener((ChannelFutureListener) future -> group.shutdownGracefully());
        } catch (Exception e) {
            log.error("client error", e);
        }
    }

}
