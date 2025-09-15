package com.mytlx.handcraft.rpc.handler;

import com.mytlx.handcraft.rpc.client.RpcClient;
import com.mytlx.handcraft.rpc.model.MessagePayload;
import com.mytlx.handcraft.rpc.model.MessageTypeEnum;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-15 13:24:32
 */
@Slf4j
@AllArgsConstructor
public class ClientHeartbeatHandler extends ChannelInboundHandlerAdapter {

    private RpcClient rpcClient;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event) {
            if (event.state() == IdleState.WRITER_IDLE) {
                MessagePayload msg = new MessagePayload()
                        .setClientId(rpcClient.getClientId())
                        .setMessageType(MessageTypeEnum.HEARTBEAT);
                log.debug("send heartbeat msg: {}", msg);
                ctx.writeAndFlush(msg);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
