package com.mytlx.reflection.practice._05_access_private_member;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 5. 访问私有成员
 * 题目：通过反射访问一个类的私有方法，并调用它。你可以定义一个 private 方法，然后尝试通过反射来调用它。
 * <pre>
 * public class MyClass {
 *     private void secretMethod() {
 *         System.out.println("This is a secret method!");
 *     }
 * }
 * </pre>
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-15 18:06
 */
public class AccessPrivateMember {

    /**
     * newInstance() 使用建议：
     * 在 newInstance() 的使用中，InstantiationException 是一种较为罕见的异常，通常建议使用 clz.getDeclaredConstructor().newInstance() 来创建类的实例，因为 newInstance() 已被弃用，且 getDeclaredConstructor() 更加符合现代 Java 的实践。
     * newInstance() 会抛出较多的异常，而 getDeclaredConstructor().newInstance() 仅会抛出少量与反射创建对象相关的异常。
     */
    public static void main(String[] args) {
        // 第一种方式
        try {
            Method secretMethod = MyClass.class.getDeclaredMethod("secretMethod");
            secretMethod.setAccessible(true);
            secretMethod.invoke(new MyClass());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        // 第二种方式
        Class<MyClass> clz = MyClass.class;
        Method[] declaredMethods = clz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().equals("secretMethod")) {
                try {
                    declaredMethod.setAccessible(true);
                    // 后续版本废弃
                    declaredMethod.invoke(clz.newInstance());
                    // 建议使用这个
                    declaredMethod.invoke(clz.getDeclaredConstructor().newInstance());
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException |
                         NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
