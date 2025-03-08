package com.mytlx.reflection.practice._03_get_field;

import com.mytlx.arcane.utils.json.gson.GsonUtils;

import java.lang.reflect.Field;

/**
 * 3. 获取字段（Field）
 * 题目：创建一个简单的类 Person，包含 private 字段 name 和 age。使用反射获取并修改这两个字段的值，然后打印出修改后的值。
 * <br/>
 * <pre>
 *      public class Person {
 *          private String name;
 *          private int age;
 *      }
 * </pre>
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-15 17:38
 */
public class GetField {

    public static void main(String[] args) {

        // 1. 第一种方式，一次性获取所有fields
        Person john = new Person("John", 30);

        Field[] fields = john.getClass().getDeclaredFields();
        System.out.println("fields = " + GsonUtils.toJson(fields));

        for (Field field : fields) {
            // 不设置会报错：
            // java.lang.IllegalAccessException: Class com.mytlx._03_get_field.GetField can not access a
            // member of class com.mytlx._03_get_field.Person with modifiers "private"
            field.setAccessible(true);
            try {
                if (field.getName().equals("name")) {
                    field.set(john, "Jane");
                } else if (field.getName().equals("age")) {
                    field.set(john, 25);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        System.out.println("john = " + GsonUtils.toJson(john));


        // 2. 第二种方式，指定名称获取field
        Person tony = new Person("Tony", 20);
        try {
            Field name = tony.getClass().getDeclaredField("name");
            Field age = tony.getClass().getDeclaredField("age");
            System.out.println("name = " + GsonUtils.toJson(name));
            System.out.println("age = " + GsonUtils.toJson(age));

            name.setAccessible(true);
            age.setAccessible(true);
            name.set(tony, "John");
            age.set(tony, 30);
            System.out.println("tony = " + GsonUtils.toJson(tony));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
