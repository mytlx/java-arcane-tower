package com.mytlx.arcane.starter.rabbitmq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytlx.arcane.starter.rabbitmq.core.RabbitReliableProducer;
import com.mytlx.arcane.starter.rabbitmq.event.MessageTransactionListener;
import com.mytlx.arcane.starter.rabbitmq.service.IRabbitMqLogService;
import com.mytlx.arcane.starter.rabbitmq.util.JsonConverter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-10-31 9:45:18
 */
@Configuration
@EnableAsync
@ComponentScan(basePackages = {"com.mytlx.arcane.starter.rabbitmq.event", "com.mytlx.arcane.starter.rabbitmq.service"})
@MapperScan(basePackages = "com.mytlx.arcane.starter.rabbitmq.mapper")
@ConditionalOnClass(RabbitTemplate.class)
@AutoConfigureAfter(RabbitAutoConfiguration.class)
public class RabbitMqAutoConfiguration {
    // 自动收集所有 IMessageLogStorageService 的实现类
    private final ObjectProvider<List<IRabbitMqLogService>> storageServicesProvider;

    public RabbitMqAutoConfiguration(ObjectProvider<List<IRabbitMqLogService>> storageServicesProvider) {
        this.storageServicesProvider = storageServicesProvider;
    }

    @Bean
    public RabbitReliableProducer rabbitReliableProducer(
            RabbitTemplate rabbitTemplate,
            ApplicationEventPublisher eventPublisher,
            JsonConverter converter) {

        // 获取所有可用的存储服务实现，即使列表为空也没关系
        List<IRabbitMqLogService> storageServices = storageServicesProvider.getIfAvailable(ArrayList::new);

        return new RabbitReliableProducer(rabbitTemplate, eventPublisher, storageServices, converter);
    }

    @Bean
    public MessageTransactionListener messageTransactionListener(RabbitReliableProducer producer) {
        return new MessageTransactionListener(producer);
    }

    @Bean
    @ConditionalOnMissingBean(JsonConverter.class)  // 如果应用自己有，就不覆盖
    public JsonConverter jsonConverter(ObjectMapper objectMapper) {
        return new JsonConverter(objectMapper);
    }
}
