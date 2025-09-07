package com.mytlx.arcane.study.netty.practice.chat.message;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;

@Data
@ToString(callSuper = true)
public class LoginRequestMessage extends Message {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String nickname;

    public LoginRequestMessage() {
    }

    public LoginRequestMessage(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    @Override
    public int getMessageType() {
        return LoginRequestMessage;
    }
}
