package com.mytlx.arcane.study.netty.practice.chat.server.handler;

import com.mytlx.arcane.study.netty.practice.chat.message.LoginRequestMessage;
import com.mytlx.arcane.study.netty.practice.chat.message.LoginResponseMessage;
import com.mytlx.arcane.study.netty.practice.chat.server.service.UserServiceFactory;
import com.mytlx.arcane.study.netty.practice.chat.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-07 14:46:47
 */
@Slf4j
@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        boolean login = UserServiceFactory.getUserService().login(msg.getUsername(), msg.getPassword());
        LoginResponseMessage resp;
        if (login) {
            // 绑定用户 channel 和 username
            SessionFactory.getSession().bind(ctx.channel(), msg.getUsername());
            resp = new LoginResponseMessage(true, "登录成功");
        } else {
            resp = new LoginResponseMessage(false, "登录失败");
        }
        ctx.writeAndFlush(resp);
    }
}
