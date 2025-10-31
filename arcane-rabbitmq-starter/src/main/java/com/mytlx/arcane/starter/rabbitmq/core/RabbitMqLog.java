package com.mytlx.arcane.starter.rabbitmq.core;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 *
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-10-31 9:46:53
 */
@TableName("t_rabbitmq_log")
@Data
@NoArgsConstructor
public class RabbitMqLog {

    @TableId
    private String id;
    private String exchange;
    private String routingKey;
    private String payload;
    private String status;
    private Integer retryCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public RabbitMqLog(ReliableMessage message) {
        this.id = message.getId();
        this.exchange = message.getExchange();
        this.routingKey = message.getRoutingKey();
        this.payload = message.getPayload();
        this.status = message.getStatus();
        this.retryCount = message.getRetryCount();
        this.createTime = message.getCreateTime();
        this.updateTime = message.getUpdateTime();
    }
}
