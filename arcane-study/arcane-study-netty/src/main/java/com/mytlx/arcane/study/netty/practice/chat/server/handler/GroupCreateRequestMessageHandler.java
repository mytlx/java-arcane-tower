package com.mytlx.arcane.study.netty.practice.chat.server.handler;

import com.mytlx.arcane.study.netty.practice.chat.message.GroupCreateRequestMessage;
import com.mytlx.arcane.study.netty.practice.chat.message.GroupCreateResponseMessage;
import com.mytlx.arcane.study.netty.practice.chat.server.session.Group;
import com.mytlx.arcane.study.netty.practice.chat.server.session.GroupSession;
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
 * @since 2025-09-07 15:40:45
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Group group = groupSession.createGroup(msg.getGroupName(), msg.getMembers());
        if (group == null) {    // 创建成功
            ctx.writeAndFlush(new GroupCreateResponseMessage(
                    true, 
                    "群组[" + msg.getGroupName() + "]创建成功")
                    // String.format("群组[%s]创建成功", msg.getGroupName()))
            );
            List<Channel> channels = groupSession.getMembersChannel(msg.getGroupName());
            channels.forEach(channel -> {
                channel.writeAndFlush(new GroupCreateResponseMessage(true,
                        String.format("您已被拉入群组[%s]", msg.getGroupName()))
                );
            });
        } else {    // 已存在
            ctx.writeAndFlush(new GroupCreateResponseMessage(
                    false,
                    String.format("群组[%s]创建失败", msg.getGroupName()))
            );
        }
    }
}
