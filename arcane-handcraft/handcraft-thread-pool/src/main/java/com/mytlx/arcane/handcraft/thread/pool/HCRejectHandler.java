package com.mytlx.arcane.handcraft.thread.pool;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-08-20 16:37:41
 */
public interface HCRejectHandler {
    void reject(Runnable rejectRunnable, HCThreadPool threadPool);
}
