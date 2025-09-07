package com.mytlx.arcane.study.netty.practice.chat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-07 20:21:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PingMessage extends Message {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
