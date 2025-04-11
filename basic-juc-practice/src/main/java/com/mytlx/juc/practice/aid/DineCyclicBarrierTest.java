package com.mytlx.juc.practice.aid;

import java.util.concurrent.*;

/**
 * 假设今天是周六，公司要求今天统一去公司集合，然后人到齐之后一起坐车去团建！！！
 * 要求所有员工必须先全部到公司集合，人到齐后再一块儿去目的地团建。
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-03-09 14:53
 */
public class DineCyclicBarrierTest {

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

    private final static CyclicBarrier CYCLIC_BARRIER = new CyclicBarrier(4, () -> {
        System.out.println("人都到齐了，出发去团建;每一个人都很开心，脸上挂着幸福的笑容.");
        System.out.println("公司班车开始发往目的地...");
        try {
            Thread.sleep((long) (Math.random() * 10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("两个小时后...");
    });

    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            EXECUTOR.submit(new Employee("打工人" + i + "号"));
        }

        EXECUTOR.shutdown();
    }

    static class Employee implements Runnable {
        private final String name;

        public Employee(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                System.out.println(name + "出发前往公司.");
                Thread.sleep((long) (Math.random() * 10000));
                System.out.println(name + "到达公司");
                CYCLIC_BARRIER.await();

                System.out.println(name + "经过2个小时的车程，到达了目的地");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

}
