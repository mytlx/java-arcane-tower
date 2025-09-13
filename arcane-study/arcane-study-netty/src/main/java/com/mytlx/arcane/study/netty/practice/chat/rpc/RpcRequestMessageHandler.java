package com.mytlx.arcane.study.netty.practice.chat.rpc;

import com.mytlx.arcane.study.netty.practice.chat.message.RpcRequestMessage;
import com.mytlx.arcane.study.netty.practice.chat.message.RpcResponseMessage;
import com.mytlx.arcane.study.netty.practice.chat.server.service.ServiceFactory;
import com.mytlx.arcane.utils.json.gson.GsonUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-08 0:23:40
 */
@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage msg) throws Exception {
        RpcResponseMessage resp = new RpcResponseMessage();
        resp.setSequenceId(msg.getSequenceId());

        try {
            HelloService service = (HelloService) ServiceFactory.getService(Class.forName(msg.getInterfaceName()));
            Method sayHello = service.getClass().getDeclaredMethod("sayHello", String.class);
            Object invoke = sayHello.invoke(service, msg.getParameterValue());
            resp.setReturnValue(invoke);
        } catch (Exception e) {
            resp.setExceptionValue(e);
            e.printStackTrace();
        }

        ctx.writeAndFlush(resp);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(GsonUtils.toJson(String.class));

        RpcRequestMessage rpcRequestMessage = new RpcRequestMessage(
                1,
                "com.mytlx.arcane.study.netty.practice.chat.rpc.HelloService",
                "sayHello",
                String.class,
                new Class[]{String.class},
                new Object[]{"zhangsan"}
        );

        System.out.println(GsonUtils.toJson(rpcRequestMessage));

    }
}
