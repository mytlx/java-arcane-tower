package com.mytlx.arcane.study.netty.practice.chat.server.session;

import lombok.Getter;

public abstract class SessionFactory {

    @Getter
    private static final Session session = new SessionMemoryImpl();

}
