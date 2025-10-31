package com.mytlx.arcane.starter.rabbitmq.event;

import com.mytlx.arcane.starter.rabbitmq.core.RabbitReliableProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 *
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-10-31 10:51:34
 */
@Component
@Slf4j
public class MessageTransactionListener {
    private final RabbitReliableProducer producer;

    public MessageTransactionListener(RabbitReliableProducer producer) {
        this.producer = producer;
    }

    /**
     * 监听 MessageSendEvent，在业务事务提交成功后发送消息
     */
    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMessageSentEvent(MessageSendEvent event) {
        log.debug("Received event {}", event);
        producer.doSend(event.getMessage(), event.getDurationType());
    }

    // 可以在这里添加 @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    // 来处理事务回滚时的日志清理或特殊通知

}
