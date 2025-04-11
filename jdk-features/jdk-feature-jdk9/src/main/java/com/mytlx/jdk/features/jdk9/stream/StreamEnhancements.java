package com.mytlx.jdk.features.jdk9.stream;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * JDK 9 为 Stream 增加了 takeWhile()、dropWhile() 和 iterate() 方法
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-03-08 12:55
 */
public class StreamEnhancements {

    public static void main(String[] args) {
        // takeWhile：获取满足条件的元素，直到遇到不满足的，后续满足的也不会处理
        Stream.of(1, 2, 3, 4, 5, 6, 2, 3)
                .takeWhile(n -> n < 4)
                .forEach(System.out::println);

        System.out.println("------");

        // dropWhile：跳过满足条件的元素，直到遇到不满足的，后续满足的不再跳过
        Stream.of(1, 2, 3, 4, 5, 6, 2, 3, 4, 5)
                .dropWhile(n -> n < 4)
                .forEach(System.out::println);

        System.out.println("------");

        // iterate() 的增强版，支持 Predicate 终止条件
        IntStream.iterate(1, n -> n < 100, n -> n * 2)
                .forEach(System.out::println);
    }


}
