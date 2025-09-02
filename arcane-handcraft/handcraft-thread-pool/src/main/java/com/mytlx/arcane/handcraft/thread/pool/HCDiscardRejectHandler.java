package com.mytlx.arcane.handcraft.thread.pool;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-08-20 16:41:51
 */
public class HCDiscardRejectHandler implements HCRejectHandler {
    @Override
    public void reject(Runnable rejectRunnable, HCThreadPool threadPool) {
        threadPool.blockingQueue.poll();
        threadPool.execute(rejectRunnable);
    }
}
