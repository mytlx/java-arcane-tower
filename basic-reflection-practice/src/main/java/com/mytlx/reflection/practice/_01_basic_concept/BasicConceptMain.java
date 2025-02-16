package com.mytlx.reflection.practice._01_basic_concept;

import com.mytlx.json.GsonUtils;

import java.lang.reflect.Constructor;

/**
 * 1. 基础概念
 * 题目：通过反射，获取并打印出 java.lang.String 类的所有构造方法（包括公共、私有等）。
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-15 14:27
 */
public class BasicConceptMain {

    /**
     * getConstructors() 方法返回的是该类的所有公共构造方法，不包括私有的或者默认的构造方法。
     * 如果你想获取所有构造方法（包括私有的、受保护的和默认的），需要使用 getDeclaredConstructors()。
     * constructor.setAccessible(true) 用于解除构造方法的访问控制，允许你访问私有和受保护的构造方法。
     */
    public static void main(String[] args) {
        Class<String> stringClass = String.class;

        Constructor<?>[] constructors = stringClass.getConstructors();
        Constructor<?>[] declaredConstructors = stringClass.getDeclaredConstructors();

        System.out.println("constructors = " + GsonUtils.toJson(constructors));
        System.out.println("declaredConstructors = " + GsonUtils.toJson(declaredConstructors));
    }

}
