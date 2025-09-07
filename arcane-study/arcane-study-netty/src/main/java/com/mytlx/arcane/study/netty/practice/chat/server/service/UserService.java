package com.mytlx.arcane.study.netty.practice.chat.server.service;

/**
 * 用户管理接口
 */
public interface UserService {

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return true - 成功 false - 失败
     */
    boolean login(String username, String password);
}
