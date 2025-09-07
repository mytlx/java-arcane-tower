package com.mytlx.arcane.study.netty.practice.chat.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-07 1:10:50
 */
public class ProtocolFrameDecoder extends LengthFieldBasedFrameDecoder {

    public ProtocolFrameDecoder() {
        this(1024, 12, 4, 0, 0);
    }

    public ProtocolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
