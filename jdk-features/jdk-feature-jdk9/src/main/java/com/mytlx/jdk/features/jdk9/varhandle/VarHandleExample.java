package com.mytlx.jdk.features.jdk9.varhandle;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

/**
 * JDK 9 引入 VarHandle，用于替代 Unsafe 进行内存操作
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-03-08 13:03
 */
public class VarHandleExample {

    private static int value = 42;
    private static final VarHandle VH;

    static {
        try {
            VH = MethodHandles.lookup().findStaticVarHandle(VarHandleExample.class, "value", int.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("Before: " + value);
        VH.setRelease(100);
        System.out.println("After: " + value);
    }

}
