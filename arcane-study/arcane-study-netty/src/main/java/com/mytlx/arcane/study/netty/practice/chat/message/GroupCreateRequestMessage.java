package com.mytlx.arcane.study.netty.practice.chat.message;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.util.Set;

@Data
@ToString(callSuper = true)
public class GroupCreateRequestMessage extends Message {
    @Serial
    private static final long serialVersionUID = 1L;

    private String groupName;
    private Set<String> members;

    public GroupCreateRequestMessage(String groupName, Set<String> members) {
        this.groupName = groupName;
        this.members = members;
    }

    @Override
    public int getMessageType() {
        return GroupCreateRequestMessage;
    }
}
