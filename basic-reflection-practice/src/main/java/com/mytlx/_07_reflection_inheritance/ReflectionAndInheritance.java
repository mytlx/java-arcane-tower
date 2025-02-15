package com.mytlx._07_reflection_inheritance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 7. 反射与继承
 * 题目：创建一个父类 Animal 和一个子类 Dog。使用反射获取父类 `Animal` 的方法，并尝试调用它。
 * <pre>
 * public class Animal {
 *     public void makeSound() {
 *         System.out.println("Animal sound");
 *     }
 * }
 *
 * public class Dog extends Animal {
 *     @Override
 *     public void makeSound() {
 *         System.out.println("Bark");
 *     }
 * }
 * </pre>
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-15 18:23
 */
public class ReflectionAndInheritance {

    public static void main(String[] args) {

        try {
            Method makeSound = Animal.class.getDeclaredMethod("makeSound");
            makeSound.invoke(new Dog());
            makeSound.invoke(new Animal());

            Method dogSound = Dog.class.getDeclaredMethod("makeSound");
            dogSound.invoke(new Dog());
            // 父类对象不能调用子类的方法，因为父类并没有定义子类的特有方法
            // java.lang.IllegalArgumentException: object is not an instance of declaring class
            // dogSound.invoke(new Animal());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
