package com.mytlx.juc.practice.thread;

import java.util.concurrent.*;

/**
 * 创建线程的方式：
 * 1. 继承Thread类
 * 2. 实现Runnable接口
 * 3. 实现Callable接口
 * 4. 线程池
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-03-25 11:50
 */
public class CreateThread {
    public static void main(String[] args) throws Exception {
        // 1. 继承Thread类
        MyThread thread1 = new MyThread("继承Thread类");
        thread1.start();
        thread1.join();
        System.out.println("------------------");
        // 2. 实现Runnable接口
        Thread thread2 = new Thread(new MyRunnable(), "实现Runnable接口");
        thread2.start();
        thread2.join();
        System.out.println("------------------");
        // 3. 实现Callable接口
        FutureTask<Integer> task = new FutureTask<>(new MyCallable());
        Thread thread3 = new Thread(task, "实现Callable接口");
        thread3.start();
        Integer i1 = task.get();
        System.out.println("task返回的值为" + i1);
        System.out.println("------------------");
        // 4. 线程池
        try (ExecutorService executorService = Executors.newCachedThreadPool(
                new ThreadFactory() {
                    private int count = 1;

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "线程池线程" + count++);
                    }
                }
        )) {
            for (int i = 0; i < 5; i++) {
                executorService.submit(new MyRunnable());
            }
            executorService.shutdown();
        }

        Thread.sleep(Long.MAX_VALUE);
    }

    static class MyThread extends Thread {
        public MyThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "running...");
        }
    }

    static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "running...");
        }
    }

    static class MyCallable implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            System.out.println(Thread.currentThread().getName() + "running...");
            return 1;
        }
    }

}
