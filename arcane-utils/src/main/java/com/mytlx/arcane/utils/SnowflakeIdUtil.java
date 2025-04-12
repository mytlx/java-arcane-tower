package com.mytlx.arcane.utils;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-12 15:35
 */
public class SnowflakeIdUtil {

    // 起始时间戳（项目初始化时间，避免生成的时间戳过长）
    private static final long START_TIMESTAMP = 1744387200000L;

    // 位数分配
    private static final long SEQUENCE_BITS = 12L;
    private static final long MACHINE_BITS = 5L;
    private static final long DATACENTER_BITS = 5L;

    // 最大值计算
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);
    private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_BITS);
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_BITS);

    // 偏移量
    private static final long MACHINE_LEFT = SEQUENCE_BITS;
    private static final long DATACENTER_LEFT = MACHINE_LEFT + MACHINE_BITS;
    private static final long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BITS;

    // 默认配置（可按需改）
    private static final long DATACENTER_ID = 1L;
    private static final long MACHINE_ID = 1L;

    private static long sequence = 0L;
    private static long lastTimestamp = -1L;

    // 禁止实例化
    private SnowflakeIdUtil() {
    }

    public static synchronized long nextId() {
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("时钟回拨，拒绝生成 ID");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT)
                | (DATACENTER_ID << DATACENTER_LEFT)
                | (MACHINE_ID << MACHINE_LEFT)
                | sequence;
    }

    private static long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    public static void main(String[] args) {
        long id = SnowflakeIdUtil.nextId();
        System.out.println("生成的ID：" + id);
    }
}

