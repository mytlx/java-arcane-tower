package com.mytlx._07_reflection_inheritance;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-15 18:23
 */
public class Dog extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Bark");
    }
}
