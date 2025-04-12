package com.mytlx.arcane.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class Async {

    private final static ForkJoinPool pool = new ForkJoinPool(100);

    public static final Consumer<Throwable> defaultExceptionHandler = throwable -> {
        log.error("run fail", throwable);
    };

    public static void execute(UnSafeCallable callable, Consumer<Throwable> exceptionHandler) {
        if (Objects.isNull(exceptionHandler)) {
            exceptionHandler = defaultExceptionHandler;
        }
        Consumer<Throwable> finalExceptionHandler = exceptionHandler;
        pool.submit(() -> {
            try {
                callable.call();
            } catch (Throwable e) {
                finalExceptionHandler.accept(e);
            }
        });
    }

    public static void execute(UnSafeCallable callable) {
        execute(callable, null);
    }

    public static void execute(UnSafeCallable... callables) {
        pool.submit(
                () -> Arrays.asList(callables).parallelStream()
                        .forEach(k -> {
                            try {
                                k.call();
                            } catch (Exception e) {
                                log.error("run fail", e);
                            }
                        })
        ).join();
    }

    @SafeVarargs
    public static <T> List<T> collectSkipNull(UnSafeSupplier<T>... suppliers) {
        return pool.submit(
                () -> Arrays.asList(suppliers).parallelStream()
                        .map(k -> {
                            try {
                                return k.get();
                            } catch (Exception e) {
                                log.error("run fail", e);
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        ).join();
    }

    public static <T> ForkJoinTask<T> submit(Callable<T> task) {
        return pool.submit(task);
    }

    public interface UnSafeCallable {
        void call() throws Exception;
    }

    public interface UnSafeSupplier<T> {
        T get() throws Exception;
    }
}
