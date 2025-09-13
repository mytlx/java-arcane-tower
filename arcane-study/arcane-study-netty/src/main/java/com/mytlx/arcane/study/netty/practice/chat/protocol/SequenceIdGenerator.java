package com.mytlx.arcane.study.netty.practice.chat.protocol;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 15:23:13
 */
public class SequenceIdGenerator {
    public static final AtomicInteger SEQUENCE_ID = new AtomicInteger(0);

    public static int next() {
        return SEQUENCE_ID.getAndIncrement();
    }
}
