package com.mytlx.reflection.practice._02_class_info;

import java.util.ArrayList;
import java.util.List;

/**
 * 2. 获取类的信息
 * 题目：使用反射获取 `java.util.ArrayList` 类的类名，并打印它是否是 `List` 的子类。
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-15 17:29
 */
public class ClassInfo {

    /**
     * isAssignableFrom() 方法用于检查类或接口是否可以被分配给当前类或接口，
     * 所以应该使用 List.class.isAssignableFrom(ArrayList.class) 来判断 ArrayList 是否是 List 的子类
     */
    public static void main(String[] args) {
        String name = ArrayList.class.getName();
        System.out.println("name = " + name);

        boolean isSubclass = List.class.isAssignableFrom(ArrayList.class);
        System.out.println("isSubclass = " + isSubclass);
    }

}
