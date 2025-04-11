package com.mytlx.jdk.features.jdk9.collection;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * JDK 9 引入了创建 不可变集合 的简洁方法
 * 创建的集合 不可修改，调用 add() 或 remove() 会抛出 UnsupportedOperationException
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-03-08 12:53
 */
public class CollectionFactoryMethods {

    public static void main(String[] args) {
        List<String> list = List.of("A", "B", "C");
        Set<Integer> set = Set.of(1, 2, 3);
        Map<String, Integer> map = Map.of("One", 1, "Two", 2, "Three", 3);

        System.out.println("List: " + list);
        System.out.println("Set: " + set);
        System.out.println("Map: " + map);

        // 创建的集合 不可修改，调用 add() 或 remove() 会抛出 UnsupportedOperationException
        list.add("test");
    }

}
