package com.mytlx.reflection.practice._04_get_method;

import com.mytlx.json.GsonUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 4. 获取方法（Method）
 * 题目：在 Person 类中增加一个方法 sayHello()，使用反射调用该方法，并传入参数 "John"，让它打印出 Hello, John!。
 * <pre>
 * public class Person {
 *     private String name;
 *     private int age;
 *
 *     public void sayHello(String name) {
 *         System.out.println("Hello, " + name + "!");
 *     }
 * }
 * </pre>
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-15 17:55
 */
public class GetMethod {

    public static void main(String[] args) {

        // 1. 第一种方式，一次性获取所有方法
        Person john = new Person("John", 30);
        Method[] methods = john.getClass().getMethods();
        System.out.println("methods = " + GsonUtils.toJson(methods));

        for (Method method : methods) {
            try {
                if (method.getName().equals("sayHello")) {
                    method.invoke(john, "John");
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        // 第二种方法，指定名称获取方法
        try {
            Person tony = new Person("Tony", 20);
            Method sayHello = tony.getClass().getMethod("sayHello", String.class);
            System.out.println("sayHello = " + GsonUtils.toJson(sayHello));
            sayHello.invoke(tony, "Marry");
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
