package com.mytlx.arcane.study.netty.practice.chat.client;

import com.mytlx.arcane.study.netty.practice.chat.protocol.Serializer;
import com.mytlx.arcane.utils.YamlUtils;

import java.util.Arrays;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-07 16:47:26
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        System.out.println(args.length);

        String name = YamlUtils.getString("serializer.algorithm");
        System.out.println("name = " + name);
        System.out.println("ordinal() = " +
                Serializer.Algorithm.valueOf(name).ordinal());

        System.out.println("Serializer.Algorithm.getCodeFromConfig() = " + Serializer.Algorithm.getCodeFromConfig());
        System.out.println("Serializer.Algorithm.getFromConfig() = " + Serializer.Algorithm.getFromConfig());

    }
}
