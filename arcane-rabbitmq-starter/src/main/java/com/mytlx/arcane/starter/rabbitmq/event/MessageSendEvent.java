package com.mytlx.arcane.starter.rabbitmq.event;

import com.mytlx.arcane.starter.rabbitmq.core.DurationType;
import com.mytlx.arcane.starter.rabbitmq.core.ReliableMessage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;

/**
 *
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-10-31 10:48:01
 */
@Getter
public class MessageSendEvent extends ApplicationEvent {
    @Serial
    private static final long serialVersionUID = 1L;

    private final ReliableMessage message;
    private final DurationType durationType;

    public MessageSendEvent(Object source, ReliableMessage message, DurationType durationType) {
        super(source);
        this.message = message;
        this.durationType = durationType;
    }
}
