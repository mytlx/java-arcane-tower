package com.mytlx.dev.solutions.drilldown.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 统计数据限制维度
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-11 20:45
 */
@Getter
@AllArgsConstructor
public enum StatisticRangeByEnum {

    BigArea(301101, "大区"), // 大区
    City(301102, "城市"), // 城市
    Company(301103, "公司"), // 公司
    Agent(301104, "经纪人"), // 经纪人
    ;

    private final int code;
    private final String name;

    public static StatisticRangeByEnum getEnumByCode(Integer code) {
        for (StatisticRangeByEnum obj : StatisticRangeByEnum.values()) {
            if (Objects.equals(obj.getCode(), code)) {
                return obj;
            }
        }
        return null;
    }

}
