package com.mytlx.handcraft.rpc.client;

import com.mytlx.handcraft.rpc.handler.ClientHeartbeatHandler;
import com.mytlx.handcraft.rpc.handler.KryoCallMessageEncoder;
import com.mytlx.handcraft.rpc.handler.KryoMessageDecoder;
import com.mytlx.handcraft.rpc.handler.RpcClientMessageHandler;
import com.mytlx.handcraft.rpc.model.MessagePayload;
import com.mytlx.handcraft.rpc.model.MessageTypeEnum;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 16:45:48
 */
@Slf4j
public class RpcClient extends AbstractRemoteClient {

    private final NioEventLoopGroup worker = new NioEventLoopGroup();

    private Channel channel;

    @Value("${handcraft.rpc.server.host}")
    private String host;

    @Value("${handcraft.rpc.server.port}")
    private int port;

    private AtomicInteger retryCount = new AtomicInteger(0);

    private int maxRetryTimes = 5;


    @PostConstruct
    public void init() {
        log.info("rpc client init");
        connect();
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void connect() {
        try {
            RpcClientMessageHandler rpcClientMessageHandler = new RpcClientMessageHandler(this);
            ClientHeartbeatHandler clientHeartbeatHandler = new ClientHeartbeatHandler(this);

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new LoggingHandler())
                                    // .addLast(new JsonCallMessageEncoder())
                                    // .addLast(new JsonMessageDecoder())
                                    .addLast(new KryoCallMessageEncoder())
                                    .addLast(new KryoMessageDecoder())
                                    .addLast(new IdleStateHandler(0, 5, 0))
                                    .addLast(clientHeartbeatHandler)
                                    .addLast(rpcClientMessageHandler)
                            ;
                            ch.pipeline().addLast(new ChannelDuplexHandler() {
                                @Override
                                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                    promise.addListener(future -> {
                                        if (!future.isSuccess()) {
                                            log.error("消息发送失败: {}", msg);
                                            log.error(future.cause().getMessage(), future.cause());
                                        }
                                    });
                                    super.write(ctx, msg, promise);
                                }
                            });
                        }
                    });
            bootstrap.connect(host, port)
                    .addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            log.info("client id: {} 连接成功", getClientId());
                            retryCount.set(0);
                            this.channel = future.channel();
                            // 连接成功，发送注册消息
                            sendRegistrationRequest();
                            future.channel().closeFuture().addListener(
                                    cf -> {
                                        log.warn("Channel closed, try to reconnect...");
                                        reconnect();
                                    }
                            );
                        } else {
                            log.error("Failed to connect to server: {}", future.cause().getMessage(), future.cause());
                            reconnect();
                        }
                    });
        } catch (Exception e) {
            log.error("Failed to connect to rpc server", e);
            throw new RuntimeException(e);
        }
    }

    public boolean reconnect() {
        if (retryCount.getAndIncrement() >= maxRetryTimes) {
            log.error("retry times has been limited: {}", maxRetryTimes);
            return false;
        }
        log.info("reconnect to server, times: {}", retryCount.get());
        worker.schedule(RpcClient.this::connect, 5, TimeUnit.SECONDS);
        return true;
    }

    public void stop() {
        log.info("正在关闭客户端 {}", getClientId());
        try {
            if (channel != null) {
                channel.close().sync();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            worker.shutdownGracefully();
        }
    }

    public void sendRegistrationRequest() {
        MessagePayload message = new MessagePayload()
                .setClientId(getClientId())
                .setMessageType(MessageTypeEnum.REGISTER);
        sendMessage(message);
    }

    @Override
    public void sendMessage(MessagePayload msg) {
        log.debug("before writeAndFlush");
        channel.writeAndFlush(msg);
        log.debug("after writeAndFlush: {}", msg);
    }
}
