package com.mytlx.arcane.study.netty.practice.chat.message;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;

@Data
@ToString(callSuper = true)
public class GroupChatResponseMessage extends AbstractResponseMessage {
    @Serial
    private static final long serialVersionUID = 1L;

    private String from;
    private String content;
    private String groupName;

    public GroupChatResponseMessage(boolean success, String msg) {
        super(success, msg);
    }

    public GroupChatResponseMessage(String from, String content) {
        this.from = from;
        this.content = content;
    }

    public GroupChatResponseMessage(String from, String content, String groupName) {
        this.from = from;
        this.content = content;
        this.groupName = groupName;
    }

    public GroupChatResponseMessage(boolean success, String msg, String from, String content, String groupName) {
        super(success, msg);
        this.from = from;
        this.content = content;
        this.groupName = groupName;
    }

    public static GroupChatResponseMessage success(String from, String content, String groupName) {
        return new GroupChatResponseMessage(true, "发送成功", from, content, groupName);
    }

    @Override
    public int getMessageType() {
        return GroupChatResponseMessage;
    }
}
