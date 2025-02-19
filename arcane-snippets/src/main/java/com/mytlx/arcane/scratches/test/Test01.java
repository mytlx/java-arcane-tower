package com.mytlx.arcane.scratches.test;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-20 0:47
 */
public class Test01 {
    public static void main(String[] args) {
        outer:
        for (int i = 0; i < 3; i++) {
            inner:
            for (int j = 0; j < 2; j++) {
                if (j == 1) {
                    continue outer;
                }
                System.out.println(j + " and " + i);
            }
        }
    }
}
