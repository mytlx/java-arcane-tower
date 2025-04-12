package com.mytlx.dev.solutions.drilldown.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 统计数据聚合维度
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-12 14:01
 */
@Getter
@AllArgsConstructor
public enum StatisticGroupByEnum {

    BigArea(301101, "大区"), // 大区
    City(301102, "城市"), // 城市
    Company(301103, "公司"), // 公司
    Agent(301104, "经纪人"), // 经纪人
    ;

    private final int code;
    private final String name;

    public static StatisticGroupByEnum getEnumByCode(Integer code) {
        for (StatisticGroupByEnum obj : StatisticGroupByEnum.values()) {
            if (Objects.equals(obj.getCode(), code)) {
                return obj;
            }
        }
        return null;
    }

}
