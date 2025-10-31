package com.mytlx.arcane.starter.rabbitmq.service;

import com.mytlx.arcane.starter.rabbitmq.core.DurationType;
import com.mytlx.arcane.starter.rabbitmq.core.ReliableMessage;

/**
 *
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-10-31 9:49:46
 */
public interface IRabbitMqLogService {

    void save(ReliableMessage message);

    void updateStatus(String messageId, String status);

    // 获取当前策略类型，用于工厂模式的 Map 查找
    DurationType getDurationType();

    // ReliableMessage findById(String messageId);
    //
    // // 查询所有 FAILED 消息，供定时重试任务使用
    // List<ReliableMessage> findFailedMessagesForRetry();
}
