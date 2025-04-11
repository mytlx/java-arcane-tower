package com.mytlx.juc.practice.aid;

import java.util.concurrent.*;

/**
 * 假设我们现在有一个任务，它在执行的时候消耗的系统资源比较大，我们的服务器经过测试一次最多执行两个任务
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-03-09 15:39
 */
public class SemaphoreTest {
    private final static ThreadPoolExecutor EXECUTOR =
            new ThreadPoolExecutor(
                    10,
                    20,
                    60,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(100),
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.AbortPolicy()
            );

    private final static Semaphore SEMAPHORE = new Semaphore(2);

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            EXECUTOR.submit(new Task());
        }
    }

    static class Task implements Runnable {
        @Override
        public void run() {
            try {
                SEMAPHORE.acquire();
                System.out.println(Thread.currentThread().getName() + "获取到了许可证，开始运行. ");
                Thread.sleep((long) (Math.random() * 10000));
                System.out.println(Thread.currentThread().getName() + "运行结束. ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                SEMAPHORE.release();
            }
        }
    }
}
