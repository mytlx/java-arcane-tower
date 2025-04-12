package com.mytlx.dev.solutions.drilldown.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 大区枚举
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-11 20:45
 */
@Getter
@AllArgsConstructor
public enum BigAreaEnum {

    BeiFang(10000000, "北方"),
    HuaBei(10000001, "华北"),
    HuaNam(10000002, "东南"),
    HuaZhong(10000003, "华中"),
    HuaXi(10000004, "华西"),
    HuaDong(10000005, "华东"),
    HuaNan(10000006, "华南"),
    ;
    private int code;
    private String name;

    public static BigAreaEnum getEnumByCode(Integer code) {

        for (var obj : BigAreaEnum.values()) {
            if (Objects.equals(obj.getCode(), code)) {
                return obj;
            }
        }
        throw new IllegalArgumentException("code can't be " + code);
    }

    public static BigAreaEnum getEnumByName(String name) {

        for (var obj : BigAreaEnum.values()) {
            if (Objects.equals(obj.getName(), name)) {
                return obj;
            }
        }
        throw new IllegalArgumentException("name can't be " + name);
    }

    public static String getNameByCode(Integer code) {

        for (var obj : BigAreaEnum.values()) {
            if (Objects.equals(obj.getCode(), code)) {
                return obj.name;
            }
        }
        throw new IllegalArgumentException("code can't be " + code);
    }

}
