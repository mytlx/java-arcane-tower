package com.mytlx.handcraft.rpc.handler;

import com.mytlx.handcraft.rpc.server.ClientSessionManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-15 13:00:46
 */
@Slf4j
public class ServerHeartbeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event) {
            if (event.state() == IdleState.READER_IDLE) {
                log.error("server heartbeat read nothing in 10s, closing");

                Channel channel = ctx.channel();
                // clear client session
                String clientId = channel.attr(AttributeKey.valueOf("clientId")).get().toString();
                ClientSessionManager.clearByClientId(clientId);

                channel.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
