package com.mytlx.arcane.study.netty.practice.chat.message;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;

@Data
@ToString(callSuper = true)
public class GroupJoinRequestMessage extends Message {
    @Serial
    private static final long serialVersionUID = 1L;

    private String groupName;
    private String username;

    public GroupJoinRequestMessage(String username, String groupName) {
        this.groupName = groupName;
        this.username = username;
    }

    @Override
    public int getMessageType() {
        return GroupJoinRequestMessage;
    }
}
