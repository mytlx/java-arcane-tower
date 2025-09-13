package com.mytlx.arcane.study.netty.practice.chat.rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-08 0:22:52
 */
@Slf4j
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("rpc response: {}",msg);
    }
}
