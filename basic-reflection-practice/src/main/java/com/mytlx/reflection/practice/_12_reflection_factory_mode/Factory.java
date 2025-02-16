package com.mytlx.reflection.practice._12_reflection_factory_mode;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * 12. 反射应用：工厂模式
 * 题目：通过反射实现一个简单的工厂模式，根据传入的类名实例化对应的对象。例如，Car 或 Bike。
 * <pre>
 * public class Car {
 *     public void drive() {
 *         System.out.println("Driving a car!");
 *     }
 * }
 *
 * public class Bike {
 *     public void ride() {
 *         System.out.println("Riding a bike!");
 *     }
 * }
 * </pre>
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-15 19:42
 */
public class Factory {

    public static <T> T createInstance(String className, Class<T> clazz) {
        try {
            Class<?> classObj = Class.forName(className); // 获取Class对象
            return clazz.cast(classObj.getDeclaredConstructor().newInstance()); // 使用构造函数来实例化对象
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Car car = createInstance("com.mytlx.reflection.practice._12_reflection_factory_mode.Car", Car.class);
        Optional.ofNullable(car).ifPresent(Car::drive);

        Bike bike = createInstance("com.mytlx.reflection.practice._12_reflection_factory_mode.Bike", Bike.class);
        Optional.ofNullable(bike).ifPresent(Bike::ride);
    }

}
