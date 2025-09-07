package com.mytlx.arcane.study.netty.practice.chat.message;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class LoginResponseMessage extends AbstractResponseMessage {
    @Override
    public int getMessageType() {
        return LoginResponseMessage;
    }
}
