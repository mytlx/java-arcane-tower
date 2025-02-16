package com.mytlx.reflection.practice._06_invoke_constructor;

import com.mytlx.json.GsonUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 6. 反射调用构造方法（Constructor）
 * 题目：创建一个类 Car，包含带参数的构造函数（比如 String model, int year）。使用反射调用该构造函数并创建 Car 对象。
 * <pre>
 * public class Car {
 *     private String model;
 *     private int year;
 *
 *     public Car(String model, int year) {
 *         this.model = model;
 *         this.year = year;
 *     }
 * }
 * </pre>
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-15 18:17
 */
public class InvokeConstructor {

    public static void main(String[] args) {
        try {
            Constructor<Car> constructor = Car.class.getConstructor(String.class, int.class);
            Car tesla = constructor.newInstance("Tesla", 2023);
            System.out.println("tesla = " + GsonUtils.toJson(tesla));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
