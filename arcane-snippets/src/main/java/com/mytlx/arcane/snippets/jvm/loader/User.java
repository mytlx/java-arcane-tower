package com.mytlx.arcane.snippets.jvm.loader;

import lombok.Data;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-03-11 16:12
 */
@Data
public class User {

    private String name;

    private int age;

    public void test() {
        System.out.println("user test");
    }

}
