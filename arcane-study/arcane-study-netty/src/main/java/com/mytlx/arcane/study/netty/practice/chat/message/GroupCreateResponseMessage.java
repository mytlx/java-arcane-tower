package com.mytlx.arcane.study.netty.practice.chat.message;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;

@Data
@ToString(callSuper = true)
public class GroupCreateResponseMessage extends AbstractResponseMessage {
    @Serial
    private static final long serialVersionUID = 1L;

    public GroupCreateResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return GroupCreateResponseMessage;
    }
}
