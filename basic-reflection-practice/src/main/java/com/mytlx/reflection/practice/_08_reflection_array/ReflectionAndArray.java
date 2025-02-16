package com.mytlx.reflection.practice._08_reflection_array;

import com.mytlx.json.GsonUtils;

import java.lang.reflect.Array;

/**
 * 8. 反射与数组
 * 题目：使用反射创建一个 int[] 数组并给它赋值。然后通过反射获取并打印出数组的元素。
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-15 18:38
 */
public class ReflectionAndArray {

    public static void main(String[] args) {
        try {
            // 创建一个 int 类型的数组，长度为 4
            int[] arr = (int[]) Array.newInstance(int.class, 4);

            // 使用反射给数组赋值
            Array.set(arr, 0, 0);  // arr[0] = 0
            Array.set(arr, 1, 1);  // arr[1] = 1
            Array.set(arr, 2, 2);  // arr[2] = 2
            Array.set(arr, 3, 3);  // arr[3] = 3

            // 打印数组的元素
            System.out.println("arr = " + GsonUtils.toJson(arr));

            // 使用反射获取数组元素并打印
            for (int i = 0; i < Array.getLength(arr); i++) {
                Object element = Array.get(arr, i);  // 获取数组第 i 个元素
                System.out.println("Element at index " + i + ": " + element);
                int anInt = Array.getInt(arr, i);// 获取数组第 i 个元素
                System.out.println("anInt = " + GsonUtils.toJson(anInt));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
