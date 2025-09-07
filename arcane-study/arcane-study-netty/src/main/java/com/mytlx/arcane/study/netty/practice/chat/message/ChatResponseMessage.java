package com.mytlx.arcane.study.netty.practice.chat.message;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;

@Data
@ToString(callSuper = true)
public class ChatResponseMessage extends AbstractResponseMessage {
    @Serial
    private static final long serialVersionUID = 1L;

    private String from;
    private String to;
    private String content;

    public ChatResponseMessage(boolean success, String msg) {
        super(success, msg);
    }

    public ChatResponseMessage(String from, String content, String to, String msg, boolean success) {
        super(success, msg);
        this.from = from;
        this.content = content;
        this.to = to;
    }

    public static ChatResponseMessage success(String from, String content, String to) {
        return new ChatResponseMessage(from, content, to, "发送成功", true);
    }

    public static ChatResponseMessage fail(String from, String content, String to, String msg) {
        return new ChatResponseMessage(from, content, to, msg, false);
    }

    @Override
    public int getMessageType() {
        return ChatResponseMessage;
    }
}
