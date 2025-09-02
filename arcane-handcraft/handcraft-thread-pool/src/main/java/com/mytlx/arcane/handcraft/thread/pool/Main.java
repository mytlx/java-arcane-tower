package com.mytlx.arcane.handcraft.thread.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-08-20 16:07:47
 */
public class Main {

    public static void main(String[] args) {
        HCThreadPool pool = new HCThreadPool(
                2,
                4,
                1,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2),
                new HCDiscardRejectHandler()
        );
        for (int i = 0; i < 5; i++) {
            pool.execute(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName());
            });
        }
        System.out.println("主线程没有被阻塞");
    }

}
