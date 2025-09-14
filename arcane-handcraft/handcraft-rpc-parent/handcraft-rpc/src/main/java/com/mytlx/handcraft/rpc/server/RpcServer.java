package com.mytlx.handcraft.rpc.server;

import com.mytlx.handcraft.rpc.handler.JsonCallMessageEncoder;
import com.mytlx.handcraft.rpc.handler.JsonMessageDecoder;
import com.mytlx.handcraft.rpc.handler.RpcServerMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-02 13:55:56
 */
@Slf4j
@Data
@Accessors(chain = true)
public class RpcServer {

    @Value("${handcraft.rpc.server.port}")
    private int port;

    @Value("${handcraft.rpc.server.worker}")
    private int workerGroupSize;

    @Value("${handcraft.rpc.server.backlog}")
    private int backlogSize;

    public void startServer() {
        new Thread(this::start).start();
    }

    private void start() {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup(workerGroupSize);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .channel(NioServerSocketChannel.class)
                    .group(boss, worker)
                    .option(ChannelOption.SO_BACKLOG, backlogSize)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new JsonMessageDecoder())
                                    .addLast(new JsonCallMessageEncoder())
                                    .addLast(new RpcServerMessageHandler())

                            ;
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            log.info("RPC server started on port {}", port);
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("RPC Server start error", e);
            throw new RuntimeException(e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }


}
