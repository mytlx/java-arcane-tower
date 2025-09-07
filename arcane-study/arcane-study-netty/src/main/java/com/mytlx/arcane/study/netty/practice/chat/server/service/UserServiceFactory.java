package com.mytlx.arcane.study.netty.practice.chat.server.service;

import lombok.Getter;

public abstract class UserServiceFactory {

    @Getter
    private static final UserService userService = new UserServiceMemoryImpl();

}
