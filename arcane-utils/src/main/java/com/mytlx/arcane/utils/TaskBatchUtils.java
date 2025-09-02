package com.mytlx.arcane.utils;

import com.mytlx.arcane.utils.json.gson.GsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-13 15:57
 */
@Slf4j
public class TaskBatchUtils {

    public static <T> void disposeLatch(List<T> tasks, Consumer<? super T> consumer, Executor executor) throws Exception {
        if (tasks == null || tasks.isEmpty()) return;
        if (consumer == null) throw new IllegalArgumentException("consumer must not be null");

        CountDownLatch latch = new CountDownLatch(tasks.size());
        for (T task : tasks) {
            executor.execute(() -> {
                try {
                    consumer.accept(task);
                } catch (Exception e) {
                    log.error("TaskBatchUtils#dispose error, task: {}", GsonUtils.toJson(task), e);
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

    public static <T> void dispose(List<T> tasks, Consumer<T> consumer, Executor executor) throws InterruptedException, ExecutionException {

        if (tasks == null || tasks.isEmpty()) return;
        if (consumer == null) throw new IllegalArgumentException("consumer must not be null");

        List<CompletableFuture<Void>> futures = tasks.stream()
                .map(task -> CompletableFuture.runAsync(
                        () -> {
                            try {
                                consumer.accept(task);
                            } catch (Exception e) {
                                throw new CompletionException(e); // 包装异常
                            }
                        },
                        executor)
                )
                .toList();

        // 等待所有任务执行完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // 可选：检查是否有异常
        for (CompletableFuture<Void> future : futures) {
            future.get(); // 如果有异常，会在这里抛出
        }
    }

    public static <T> void dispose(List<T> tasks, Consumer<T> consumer, ExecutorService executor) throws InterruptedException, ExecutionException {

        if (tasks == null || tasks.isEmpty()) return;
        if (consumer == null) throw new IllegalArgumentException("consumer must not be null");

        List<Callable<Void>> callables = tasks.stream()
                .map(task -> (Callable<Void>) () -> {
                    consumer.accept(task);
                    return null;
                })
                .collect(Collectors.toList());

        List<Future<Void>> futures = executor.invokeAll(callables);

        for (Future<Void> future : futures) {
            future.get(); // 捕获异常或阻塞直到完成
        }
    }

    public static <T, R> List<R> executeWithResult(List<T> tasks, Function<T, R> function, Executor executor)
            throws InterruptedException, ExecutionException {

        List<CompletableFuture<R>> futures = tasks.stream()
                .map(task -> CompletableFuture.supplyAsync(() -> function.apply(task), executor))
                .toList();

        // 等待所有任务完成并收集结果
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        List<R> results = new ArrayList<>();
        for (CompletableFuture<R> future : futures) {
            results.add(future.get());
        }

        return results;
    }



}
