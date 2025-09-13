package com.mytlx.arcane.study.netty.practice.chat.rpc;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 10:05:17
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }

}
