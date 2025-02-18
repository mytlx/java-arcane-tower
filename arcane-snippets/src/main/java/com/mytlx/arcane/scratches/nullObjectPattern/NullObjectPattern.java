package com.mytlx.arcane.scratches.nullObjectPattern;

/**
 * 空对象模式
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-18 22:32
 */
public class NullObjectPattern {

    public static void main(String[] args) {
        // 无需再判空
        new MyParser().findAction(null).execute();
    }

}
