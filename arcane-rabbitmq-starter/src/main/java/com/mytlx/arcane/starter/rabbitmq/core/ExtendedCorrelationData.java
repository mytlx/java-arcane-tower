package com.mytlx.arcane.starter.rabbitmq.core;

import lombok.Getter;
import org.springframework.amqp.rabbit.connection.CorrelationData;

/**
 *
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-10-31 11:00:30
 */
@Getter
public class ExtendedCorrelationData extends CorrelationData {

    private final DurationType durationType;

    public ExtendedCorrelationData(String id, DurationType durationType) {
        super(id);
        this.durationType = durationType;
    }

}
