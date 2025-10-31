package com.mytlx.arcane.starter.rabbitmq.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytlx.arcane.starter.rabbitmq.event.MessageSendEvent;
import com.mytlx.arcane.starter.rabbitmq.service.IRabbitMqLogService;
import com.mytlx.arcane.starter.rabbitmq.util.JsonConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-10-31 9:37:55
 */
@Slf4j
public class RabbitReliableProducer implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ApplicationEventPublisher eventPublisher;
    private final Map<String, IRabbitMqLogService> serviceMap;
    private final JsonConverter jsonConverter;

    public RabbitReliableProducer(RabbitTemplate rabbitTemplate,
                                  ApplicationEventPublisher eventPublisher,
                                  List<IRabbitMqLogService> serviceList,
                                  JsonConverter jsonConverter) {
        this.rabbitTemplate = rabbitTemplate;
        this.eventPublisher = eventPublisher;
        this.jsonConverter = jsonConverter;
        // 设置回调
        this.rabbitTemplate.setConfirmCallback(this);
        this.rabbitTemplate.setReturnsCallback(this);
        this.serviceMap = serviceList.stream()
                .collect(Collectors.toMap(
                        service -> service.getDurationType().name(),
                        Function.identity()
                ));
    }

    /**
     * 发送消息，通常在业务事务提交后调用
     */
    public <T> void send(String exchange, String routingKey, T data, String messageId, DurationType type) {
        CorrelationData correlationData = new CorrelationData(messageId);
        String payload = jsonConverter.toJson(data);

        // 1. 不需要持久化，直接发送
        if (type == DurationType.NONE) {
            rabbitTemplate.convertAndSend(exchange, routingKey, payload,
                    createTrackingPostProcessor(messageId, type), correlationData);
            log.info("NONE 模式消息发送，ID: {}, Exchange: {}, routingKey: {}", messageId, exchange, routingKey);
            return;
        }
        // 2. 需要持久化 (MYSQL/REDIS)
        IRabbitMqLogService service = serviceMap.get(type.name());
        if (service == null) {
            log.error("未找到存储策略实现：{}，消息发送失败。", type.name());
            throw new IllegalArgumentException("未找到消息日志存储实现：" + type.name());
        }

        try {
            ReliableMessage message = new ReliableMessage();
            message.setId(messageId);
            message.setExchange(exchange);
            message.setRoutingKey(routingKey);
            message.setPayload(payload);
            message.setStatus(ReliableMessage.STATUS_PENDING);
            message.setDurationType(type);

            // 存入日志（应与业务事务在同一线程中，但不在同一事务中）
            service.save(message);

            // 事务提交后发送，利用 Spring 事务事件解耦
            eventPublisher.publishEvent(new MessageSendEvent(this, message, type));
            log.debug("publishEvent, message: {}, type: {}", message, type);
        } catch (Exception e) {
            log.error("消息落库序列化失败或发送异常。", e);
            throw new RuntimeException("消息可靠性发送失败", e);
        }
    }

    // 实际执行发送动作的方法 (被事务监听器调用)
    public void doSend(ReliableMessage message, DurationType durationType) {
        log.info("消息发送，ID: {}, Exchange: {}, routingKey: {}", message.getId(), message.getExchange(), message.getRoutingKey());
        ExtendedCorrelationData correlationData = new ExtendedCorrelationData(message.getId(), durationType);
        rabbitTemplate.convertAndSend(
                message.getExchange(),
                message.getRoutingKey(),
                message.getPayload(),
                createTrackingPostProcessor(message.getId(), durationType),
                correlationData);
    }

    // 辅助方法：创建 MessagePostProcessor 嵌入追踪信息
    private MessagePostProcessor createTrackingPostProcessor(String messageId, DurationType type) {
        return message -> {
            // 将 ID 和 Type 嵌入消息头
            message.getMessageProperties().setHeader("X-Message-ID", messageId);
            message.getMessageProperties().setHeader("X-Storage-Type", type.name());
            return message;
        };
    }

    /**
     * 消息到达交换机回调 (Publisher Confirms)
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!(correlationData instanceof ExtendedCorrelationData extendedData)) {
            // 确保是自定义对象，否则可能是其他非可靠消息的回调
            log.warn("收到非 ExtendedCorrelationData 类型回调，忽略。ID: {}", correlationData != null ? correlationData.getId() : "null");
            return;
        }
        String messageId = extendedData.getId();
        DurationType type = extendedData.getDurationType();
        if (type == DurationType.NONE) {
            log.info("NONE 模式确认，ID: {}，结果: {}", messageId, ack ? "成功" : "失败");
            return; // NONE 模式不需要更新 DB/Redis
        }

        // 持久化模式的回调
        IRabbitMqLogService service = serviceMap.get(type.name());
        if (service == null) return;

        if (ack) {
            // 成功到达，从 Redis/DB 中删除或标记 SENT
            service.updateStatus(messageId, ReliableMessage.STATUS_SENT);
        } else {
            // 失败，标记 FAILED，等待重试
            service.updateStatus(messageId, ReliableMessage.STATUS_FAILED);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        // 1. 获取原始消息和追踪信息
        Message message = returned.getMessage();
        Map<String, Object> headers = message.getMessageProperties().getHeaders();

        String messageId = (String) headers.get("X-Message-ID");
        String typeStr = (String) headers.get("X-Storage-Type");

        // 如果没有追踪信息（例如不是通过我们 Producer 发送的），则忽略
        if (messageId == null || typeStr == null) {
            log.warn("收到无法追踪的退回消息，Exchange: {}, RoutingKey: {}, Reason: {}",
                    returned.getExchange(), returned.getRoutingKey(), returned.getReplyText());
            return;
        }

        DurationType type = DurationType.valueOf(typeStr);

        // 2. 处理 NONE 模式
        if (type == DurationType.NONE) {
            log.warn("NONE 模式退回，ID: {}，原因: {}", messageId, returned.getReplyText());
            return;
        }

        // 3. 处理持久化模式 (MYSQL/REDIS)
        IRabbitMqLogService service = serviceMap.get(type.name());
        if (service == null) return;

        log.error("消息退回（未路由到队列），ID: {}，存储类型: {}，原因: {}", messageId, type, returned.getReplyText());

        // 退回意味着未到达队列，标记为 FAILED，等待重试
        service.updateStatus(messageId, ReliableMessage.STATUS_FAILED);
    }
}
