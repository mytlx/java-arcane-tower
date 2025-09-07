package com.mytlx.arcane.study.netty.base.netty.eventloop;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-05 23:00:55
 */
@Slf4j
public class TestEventLoop {

    public static void main(String[] args) {
        // 1. 创建事件循环组
        EventLoopGroup group = new NioEventLoopGroup(2);    // io事件、普通任务、定时任务
        // EventLoopGroup group2 = new DefaultEventLoopGroup();    // 普通任务、定时任务

        // 2. 获取下一个事件循环对象
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        // 3. 执行普通任务
        group.next().execute(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug("执行普通任务");
        });

        // 4. 执行定时任务
        group.next().scheduleAtFixedRate(() -> {
            log.debug("执行定时任务");
        }, 2, 1, TimeUnit.SECONDS);


        log.debug("main");
    }
}
