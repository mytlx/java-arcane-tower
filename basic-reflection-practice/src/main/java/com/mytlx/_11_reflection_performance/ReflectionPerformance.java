package com.mytlx._11_reflection_performance;

import java.lang.reflect.Method;

/**
 * 11. 反射性能测试
 * 题目：用反射调用一个方法和直接调用这个方法，比较二者的执行时间差异，分析反射带来的性能损耗。
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-15 19:28
 */
public class ReflectionPerformance {

    public void hello() {
        // System.out.println("Hello, world!");
    }

    public static void main(String[] args) throws Exception {
        ReflectionPerformance instance = new ReflectionPerformance();
        long start1 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            instance.hello();
        }
        long end1 = System.currentTimeMillis();

        // 使用反射
        Method method = ReflectionPerformance.class.getMethod("hello");
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            method.invoke(instance);
        }
        long end2 = System.currentTimeMillis();

        System.out.println("direct Method execution time: " + (end1 - start1) + " ms");
        System.out.println("reflection Method execution time: " + (end2 - start2) + " ms");
    }

}
