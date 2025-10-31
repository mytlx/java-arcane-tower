package com.mytlx.arcane.starter.rabbitmq.core;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-10-31 9:36:11
 */
@Data
public class ReliableMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    private String id;               // 消息唯一ID (CorrelationData)
    private String exchange;         // 交换机
    private String routingKey;       // 路由键
    private String payload;          // JSON 格式的消息体
    private DurationType durationType; // 记录日志的存储类型
    private String status;           // 状态: PENDING, SENT, FAILED, FINAL_FAILED
    private Integer retryCount = 0;  // 重试次数
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 状态常量
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_SENT = "SENT";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_FINAL_FAILED = "FINAL_FAILED";
}
