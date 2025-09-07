package com.mytlx.arcane.study.netty.practice.chat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LoginResponseMessage extends AbstractResponseMessage {
    @Serial
    private static final long serialVersionUID = 1L;

    public LoginResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return LoginResponseMessage;
    }
}
