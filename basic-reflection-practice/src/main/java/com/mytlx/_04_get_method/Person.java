package com.mytlx._04_get_method;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-15 17:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private String name;
    private int age;

    public void sayHello(String name) {
        System.out.println("Hello, " + name + "!");
    }
}
