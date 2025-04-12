package com.mytlx.arcane.utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-12 15:37
 */
class SnowflakeIdUtilTest {

    @Test
    public void generate() throws Exception {
        System.out.println(SnowflakeIdUtil.nextId());
    }

    @Test
    public void generateList() throws Exception {
        List<Long> list = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            list.add(SnowflakeIdUtil.nextId());
        }
        list.stream().map(l -> l + "L").forEach(System.out::println);
    }


}