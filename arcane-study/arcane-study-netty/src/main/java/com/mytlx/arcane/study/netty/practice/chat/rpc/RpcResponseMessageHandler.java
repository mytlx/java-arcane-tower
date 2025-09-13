package com.mytlx.arcane.study.netty.practice.chat.rpc;

import com.mytlx.arcane.study.netty.practice.chat.message.RpcResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-08 0:22:52
 */
@Slf4j
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {

    // sequenceId -> 接收结果的 promise 对象
    public static final Map<Integer, Promise<Object>> PROMISE_MAP = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {
        log.debug("rpc response: {}",msg);

        Promise<Object> promise = PROMISE_MAP.remove(msg.getSequenceId());
        if (promise != null) {
            Exception exceptionValue = msg.getExceptionValue();
            if (exceptionValue != null) {
                promise.setFailure(exceptionValue);
            } else {
                promise.setSuccess(msg.getReturnValue());
            }
        }
    }
}
