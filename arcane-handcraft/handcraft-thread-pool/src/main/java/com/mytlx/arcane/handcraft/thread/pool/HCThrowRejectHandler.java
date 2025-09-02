package com.mytlx.arcane.handcraft.thread.pool;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-08-20 16:40:51
 */
public class HCThrowRejectHandler implements HCRejectHandler {
    @Override
    public void reject(Runnable rejectRunnable, HCThreadPool threadPool) {
        throw new RuntimeException("线程池已满");
    }
}
