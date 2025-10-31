package com.mytlx.arcane.starter.rabbitmq.core;

/**
 *
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-10-31 10:15:52
 */
public enum DurationType {
    // 1. 无需持久化，直接发送（性能优先）
    NONE,

    // 2. 高性能内存存储（推荐高并发日志）
    REDIS,

    // 3. 高可靠关系型数据库存储（推荐重要、低频消息）
    MYSQL,

    // 4. 可扩展文档型数据库存储
    MONGODB,

    // 5. 自定义
    CUSTOMER,
}
