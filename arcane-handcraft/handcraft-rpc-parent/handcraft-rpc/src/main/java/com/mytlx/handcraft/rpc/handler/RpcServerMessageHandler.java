package com.mytlx.handcraft.rpc.handler;

import com.mytlx.handcraft.rpc.model.MessagePayload;
import com.mytlx.handcraft.rpc.model.MessageType;
import com.mytlx.handcraft.rpc.server.ClientSessionManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 20:16:59
 */
public class RpcServerMessageHandler extends SimpleChannelInboundHandler<MessagePayload> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessagePayload msg) throws Exception {
        MessageType messageType = msg.getMessageType();

        switch (messageType) {
            // register: save client info
            case REGISTER -> registerClientIntoSession(msg, ctx.channel());
            // call: RpcRequest info forward to other service
            case CALL -> {
                MessagePayload.RpcRequest request = (MessagePayload.RpcRequest) msg.getPayload();
                if (msg.getClientId().equals(request.getRequestClientId())) {
                    throw new RuntimeException("Client id and the request client id cannot be the same.");
                }
                Channel channel = ClientSessionManager.getChannel(request.getRequestClientId());
                if (channel == null) {
                    throw new RuntimeException("request service: " + request.getRequestClientId() + " not registered");
                }

                forwardRequestToClient(msg, channel);
            }
            // response: result back in the same way
            case RESPONSE -> {
                returnResponseToClient(msg);
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 断开连接时，清理session
        Object clientIdObj = ctx.channel().attr(AttributeKey.valueOf("clientId")).get();
        if (clientIdObj != null) {
            ClientSessionManager.clearByClientId(clientIdObj.toString());
        }
    }

    private void registerClientIntoSession(MessagePayload msg, Channel channel) {
        ClientSessionManager.register(msg.getClientId(), channel);
        // 注册时保存 clientId，用于断开后的清理工作
        channel.attr(AttributeKey.valueOf("clientId")).set(msg.getClientId());
    }

    private void forwardRequestToClient(MessagePayload msg, Channel channel) {
        ClientSessionManager.putRequest(msg);
        msg.setMessageType(MessageType.FORWARD);
        channel.writeAndFlush(msg);
    }

    private void returnResponseToClient(MessagePayload msg) {
        MessagePayload.RpcResponse response = (MessagePayload.RpcResponse) msg.getPayload();
        MessagePayload request = ClientSessionManager.getRequest(response.getRequestId());
        // 请求方的channel
        Channel channel = ClientSessionManager.getChannel(request.getClientId());
        channel.writeAndFlush(response);
    }


}
