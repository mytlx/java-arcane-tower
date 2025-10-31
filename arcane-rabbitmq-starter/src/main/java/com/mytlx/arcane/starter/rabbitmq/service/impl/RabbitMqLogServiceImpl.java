package com.mytlx.arcane.starter.rabbitmq.service.impl;

import com.mytlx.arcane.starter.rabbitmq.core.DurationType;
import com.mytlx.arcane.starter.rabbitmq.core.RabbitMqLog;
import com.mytlx.arcane.starter.rabbitmq.core.ReliableMessage;
import com.mytlx.arcane.starter.rabbitmq.mapper.RabbitMqLogMapper;
import com.mytlx.arcane.starter.rabbitmq.service.IRabbitMqLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-10-31 9:51:03
 */
@Service
@Slf4j
public class RabbitMqLogServiceImpl implements IRabbitMqLogService {

    private final RabbitMqLogMapper mapper;

    public RabbitMqLogServiceImpl(RabbitMqLogMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void save(ReliableMessage message) {
        log.debug("save message: {}", message);
        message.setCreateTime(LocalDateTime.now());
        message.setUpdateTime(LocalDateTime.now());
        mapper.insert(new RabbitMqLog(message));
    }

    @Override
    @Transactional
    public void updateStatus(String messageId, String status) {
        log.debug("updateStatus messageId={}, status={}", messageId, status);
        RabbitMqLog log = mapper.selectById(messageId);
        if (log != null) {
            RabbitMqLog update = new RabbitMqLog();
            update.setId(log.getId());
            update.setStatus(status);
            update.setUpdateTime(LocalDateTime.now());
            mapper.updateById(update);
        }
    }

    @Override
    public DurationType getDurationType() {
        return DurationType.MYSQL;
    }
}
