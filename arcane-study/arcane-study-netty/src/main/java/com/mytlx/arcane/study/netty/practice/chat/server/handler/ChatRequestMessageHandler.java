package com.mytlx.arcane.study.netty.practice.chat.server.handler;

import com.mytlx.arcane.study.netty.practice.chat.message.ChatRequestMessage;
import com.mytlx.arcane.study.netty.practice.chat.message.ChatResponseMessage;
import com.mytlx.arcane.study.netty.practice.chat.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-07 14:48:51
 */
@Slf4j
@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        Channel dstChannel = SessionFactory.getSession().getChannel(msg.getTo());
        if (dstChannel != null) {   // 在线
            dstChannel.writeAndFlush(ChatResponseMessage.success(msg.getFrom(), msg.getContent(), msg.getTo()));
        } else {    // 不在线
            ctx.writeAndFlush(ChatResponseMessage.fail(msg.getFrom(), msg.getContent(), msg.getTo(), "用户不存在或不在线"));
        }
    }
}
