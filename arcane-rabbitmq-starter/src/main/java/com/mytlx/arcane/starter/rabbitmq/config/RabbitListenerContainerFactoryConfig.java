// package com.mytlx.arcane.starter.rabbitmq.config;
//
// import org.springframework.amqp.core.AcknowledgeMode;
// import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
// import org.springframework.amqp.rabbit.connection.ConnectionFactory;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// /**
//  * @author TLX
//  * @version 1.0.0
//  * @since 2025-10-31 12:49:13
//  */
// @Configuration
// public class RabbitListenerContainerFactoryConfig {
//     @Bean(name = "rabbitListenerContainerFactory")
//     public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
//         SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//         factory.setConnectionFactory(connectionFactory);
//         factory.setConcurrentConsumers(1);
//         factory.setMaxConcurrentConsumers(1);
//         factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
//         return factory;
//     }
// }
