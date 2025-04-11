package com.mytlx.juc.practice.aid;

import lombok.AllArgsConstructor;

import java.util.concurrent.*;

/**
 * 现在公司有一个需求，需要 4 名程序员在获取到产品经理的原型和 PRD 后才能开始开发，开发完成后开始安排 1 名运维上线。
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-03-09 15:08
 */
public class DevelopCountDownLatchTest {

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

    private final static CountDownLatch PRD_LATCH = new CountDownLatch(1);

    private final static CountDownLatch DEVELOP_LATCH = new CountDownLatch(4);

    public static void main(String[] args) {
        EXECUTOR.submit(new Demander("产品经理"));
        for (int i = 0; i < 4; i++) {
            EXECUTOR.submit(new Develop("程序员" + i + "号"));
        }
        EXECUTOR.submit(new Maintainer("运维"));
        EXECUTOR.shutdown();
    }

    @AllArgsConstructor
    static class Demander implements Runnable {

        private final String name;

        @Override
        public void run() {
            try {
                System.out.println(name + "产品经理此时正在紧张的设计原型和PRD.....");
                TimeUnit.SECONDS.sleep(3);
                System.out.println(name + "原型设计完毕.");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                PRD_LATCH.countDown();
            }
        }
    }

    @AllArgsConstructor
    static class Develop implements Runnable {
        private final String name;

        @Override
        public void run() {
            try {
                System.out.println(name + "正在等待产品经理的原型和PRD...");
                PRD_LATCH.await();
                System.out.println(name + "获取了原型和PRD，开始开发.");
                Thread.sleep((long) (Math.random() * 10000));
                System.out.println(name + "开发完成.");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                DEVELOP_LATCH.countDown();
            }
        }
    }

    @AllArgsConstructor
    static class Maintainer implements Runnable {
        private final String name;

        @Override
        public void run() {
            try {
                System.out.println(name + "正在等待开发完成...");
                DEVELOP_LATCH.await();
                System.out.println("项目开发完毕，运维" + name + "开始上线.");
                System.out.println("上线成功..");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
