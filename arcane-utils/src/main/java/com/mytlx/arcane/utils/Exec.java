package com.mytlx.arcane.utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Exec {

    private static final ScheduledThreadPoolExecutor EXEC = new ScheduledThreadPoolExecutor(
            10,
            new ThreadFactory() {
                private final ThreadGroup group = Thread.currentThread().getThreadGroup();
                private final AtomicInteger threadNumber = new AtomicInteger(1);
                private final String namePrefix = "Arcane-Exec-";

                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
                    if (t.isDaemon())
                        t.setDaemon(false);
                    if (t.getPriority() != Thread.NORM_PRIORITY)
                        t.setPriority(Thread.NORM_PRIORITY);
                    return t;
                }
            });

    public static ScheduledFuture<?> schedule(Runnable callable,
                                              long delay,
                                              TimeUnit unit) {
        return EXEC.schedule(callable, delay, unit);
    }

    public static void execute(Runnable runnable) {

        EXEC.execute(runnable);
    }

    public static <T> Future<T> submit(Callable<T> task) {

        return EXEC.submit(task);
    }

    public static void scheduleAtFixedRate(Runnable command,
                                           long period,
                                           TimeUnit unit) {

        EXEC.scheduleAtFixedRate(command, 0, period, unit);
    }

    public static void scheduleAtFixedRate(Runnable command,
                                           long initialDelay,
                                           long period,
                                           TimeUnit unit) {

        EXEC.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    public static void scheduleWithFixedDelay(Runnable command,
                                              long period,
                                              TimeUnit unit) {

        EXEC.scheduleWithFixedDelay(command, 0, period, unit);
    }

    public static void scheduleWithFixedDelay(Runnable command,
                                              long initialDelay,
                                              long period,
                                              TimeUnit unit) {

        EXEC.scheduleWithFixedDelay(command, initialDelay, period, unit);
    }

    public static void shutdown() {

        EXEC.shutdown();
    }

    public static void awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {

        EXEC.awaitTermination(timeout, unit);
    }

}
