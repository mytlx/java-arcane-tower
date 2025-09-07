package com.mytlx.arcane.study.netty.practice.chat.message;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.util.Set;

@Data
@ToString(callSuper = true)
public class GroupMembersResponseMessage extends Message {
    @Serial
    private static final long serialVersionUID = 1L;

    private Set<String> members;

    public GroupMembersResponseMessage(Set<String> members) {
        this.members = members;
    }

    @Override
    public int getMessageType() {
        return GroupMembersResponseMessage;
    }
}
