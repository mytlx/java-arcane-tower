package com.mytlx.handcraft.rpc.handler;

import com.mytlx.handcraft.rpc.client.RpcClient;
import com.mytlx.handcraft.rpc.model.MessagePayload;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-14 12:15:57
 */
@Slf4j
@AllArgsConstructor
public class RpcClientMessageHandler extends SimpleChannelInboundHandler<MessagePayload> {

    private RpcClient rpcClient;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessagePayload msg) throws Exception {

        try {
            switch (msg.getMessageType()) {
                case FORWARD -> // 消息转发请求 请求调用
                        handleRequest(msg);
                case RESPONSE -> // 请求返回
                        handleResponse(msg);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private void handleRequest(MessagePayload msg) {
        rpcClient.handleRequest(((MessagePayload.RpcResponse) msg.getPayload()));
    }

    private void handleResponse(MessagePayload msg) {
        rpcClient.handleResponse(((MessagePayload.RpcRequest) msg.getPayload()));
    }
}
