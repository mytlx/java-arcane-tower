package com.mytlx.arcane.study.netty.practice.chat.server.handler;

import com.mytlx.arcane.study.netty.practice.chat.message.GroupChatRequestMessage;
import com.mytlx.arcane.study.netty.practice.chat.message.GroupChatResponseMessage;
import com.mytlx.arcane.study.netty.practice.chat.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-07 18:51:03
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        List<Channel> channels = GroupSessionFactory.getGroupSession().getMembersChannel(msg.getGroupName());
        channels.forEach(channel -> {
            channel.writeAndFlush(GroupChatResponseMessage.success(msg.getFrom(), msg.getContent(), msg.getGroupName()));
        });
    }
}
