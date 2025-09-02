package com.mytlx.arcane.handcraft.thread.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-08-20 16:07:57
 */
public class HCThreadPool {

    private final int corePoolSize;
    private final int maxPoolSize;
    private final int timeout;
    private final TimeUnit timeUnit;
    public final BlockingQueue<Runnable> blockingQueue;
    private final HCRejectHandler rejectHandler;

    public HCThreadPool(int corePoolSize, int maxPoolSize, int timeout,
                        TimeUnit timeUnit, BlockingQueue<Runnable> blockingQueue, HCRejectHandler rejectHandler) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.blockingQueue = blockingQueue;
        this.rejectHandler = rejectHandler;
    }

    List<Thread> coreList = new ArrayList<>();
    List<Thread> supportList = new ArrayList<>();

    void execute(Runnable command) {
        if (coreList.size() < corePoolSize) {
            Thread thread = new CoreThread(command);
            coreList.add(thread);
            thread.start();
        }

        if (blockingQueue.offer(command)) {
            return;
        }

        if (coreList.size() + supportList.size() < maxPoolSize) {
            Thread thread = new SupportThread(command);
            supportList.add(thread);
            thread.start();

        }

        if (!blockingQueue.offer(command)) {
            rejectHandler.reject(command, this);
        }
    }

    class CoreThread extends Thread {

        private final Runnable firstTask;

        public CoreThread(Runnable firstTask) {
            this.firstTask = firstTask;
        }

        @Override
        public void run() {
            firstTask.run();
            while (true) {
                try {
                    Runnable runnable = blockingQueue.take();
                    runnable.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    class SupportThread extends Thread {

        private final Runnable firstTask;

        public SupportThread(Runnable firstTask) {
            this.firstTask = firstTask;
        }

        @Override
        public void run() {
            firstTask.run();
            while (true) {
                try {
                    Runnable runnable = blockingQueue.poll(timeout, timeUnit);
                    if (runnable == null) {
                        break;
                    }
                    runnable.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(Thread.currentThread().getName() + "结束了");
            supportList.remove(Thread.currentThread());
        }
    }

}
