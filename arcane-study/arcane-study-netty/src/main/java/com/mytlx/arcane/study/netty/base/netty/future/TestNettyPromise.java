package com.mytlx.arcane.study.netty.base.netty.future;

import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-07 12:30:22
 */
@Slf4j
public class TestNettyPromise {

    public static void main(String[] args) throws Exception {
        test01();
        // test02();
        // test03();
        // test04();
        // test05();
        // test06();
    }

    /**
     * 同步处理任务成功
     */
    public static void test01() throws Exception {
        log.debug("=================== {} ===================", Thread.currentThread().getStackTrace()[1].getMethodName());
        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventExecutors);

        eventExecutors.execute(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("set success, {}",10);
            promise.setSuccess(10);
        });

        log.debug("start...");
        log.debug("{}",promise.getNow()); // 还没有结果 null
        log.debug("{}",promise.get());
    }

    /**
     * 异步处理任务成功
     */
    public static void test02() throws Exception {
        log.debug("=================== {} ===================", Thread.currentThread().getStackTrace()[1].getMethodName());
        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventExecutors);

        // 设置回调，异步接收结果
        promise.addListener(future -> {
            // 这里的 future 就是上面的 promise
            log.debug("{}",future.getNow());
        });

        // 等待 1000 后设置成功结果
        eventExecutors.execute(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("set success, {}",10);
            promise.setSuccess(10);
        });

        log.debug("start...");
    }

    /**
     * 同步处理任务失败 - sync & get
     */
    public static void test03() throws Exception {
        log.debug("=================== {} ===================", Thread.currentThread().getStackTrace()[1].getMethodName());
        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventExecutors);

        eventExecutors.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RuntimeException e = new RuntimeException("error...");
            log.debug("set failure, {}", e.toString());
            promise.setFailure(e);
        });

        log.debug("start...");
        log.debug("{}", promise.getNow());
        promise.get(); // sync() 也会出现异常，只是 get 会再用 ExecutionException 包一层异常
    }

    /**
     * 同步处理任务失败 - await
     */
    public static void test04() throws Exception {
        log.debug("=================== {} ===================", Thread.currentThread().getStackTrace()[1].getMethodName());
        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventExecutors);

        eventExecutors.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RuntimeException e = new RuntimeException("error...");
            log.debug("set failure, {}", e.toString());
            promise.setFailure(e);
        });

        log.debug("start...");
        log.debug("{}", promise.getNow());
        promise.await(); // 与 sync 和 get 区别在于，不会抛异常
        log.debug("result {}", (promise.isSuccess() ? promise.getNow() : promise.cause()).toString());
    }

    /**
     * 异步处理任务失败
     */
    public static void test05() throws Exception {
        log.debug("=================== {} ===================", Thread.currentThread().getStackTrace()[1].getMethodName());
        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventExecutors);

        promise.addListener(future -> {
            log.debug("result {}", (promise.isSuccess() ? promise.getNow() : promise.cause()).toString());
        });

        eventExecutors.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RuntimeException e = new RuntimeException("error...");
            log.debug("set failure, {}", e.toString());
            promise.setFailure(e);
        });

        log.debug("start...");
    }

    /**
     * await 死锁检查
     */
    public static void test06() throws Exception {
        log.debug("=================== {} ===================", Thread.currentThread().getStackTrace()[1].getMethodName());
        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventExecutors);

        eventExecutors.submit(()->{
            System.out.println("1");
            try {
                promise.await();
                // 注意不能仅捕获 InterruptedException 异常
                // 否则 死锁检查抛出的 BlockingOperationException 会继续向上传播
                // 而提交的任务会被包装为 PromiseTask，它的 run 方法中会 catch 所有异常然后设置为 Promise 的失败结果而不会抛出
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("2");
        });
        eventExecutors.submit(()->{
            System.out.println("3");
            try {
                promise.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("4");
        });
    }

}
