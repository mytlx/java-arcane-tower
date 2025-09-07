package com.mytlx.arcane.study.netty.practice.chat.message;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;

@Data
@ToString(callSuper = true)
public abstract class AbstractResponseMessage extends Message {
    @Serial
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String msg;

    public AbstractResponseMessage() {
    }

    public AbstractResponseMessage(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }
}
