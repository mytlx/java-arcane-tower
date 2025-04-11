package com.mytlx.arcane.snippets.test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-03-24 14:29
 */
public class Test02 {
    public static void main(String[] args) throws Throwable {
        int rs = Integer.numberOfLeadingZeros(32) | (1 << (16 - 1));
        System.out.println("rs = " + rs);
        int sc = (rs << 16) + 2;
        System.out.println("sc = " + sc);
        int threadCnt = 2145779710 - 1;
        System.out.println("threadCnt = " + threadCnt);
        System.out.println("Integer.MAX_VALUE = " + Integer.MAX_VALUE);

        // 获取 MethodHandles.Lookup 实例，使用 privateLookupIn 来访问私有方法
        MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(ConcurrentHashMap.class, MethodHandles.lookup());

        // 获取 resizeStamp 方法的 MethodHandle
        MethodHandle resizeStampMethod = lookup.findStatic(
                ConcurrentHashMap.class,  // 类
                "resizeStamp",            // 方法名
                MethodType.methodType(int.class, int.class)  // 方法签名
        );

        // 调用 resizeStamp 方法
        int n = 32; // 传入容量
        int stamp = (int) resizeStampMethod.invoke(n);  // 调用方法
        System.out.println("stamp = " + stamp); // 输出结果
    }
}
