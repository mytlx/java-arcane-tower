package com.mytlx.dev.solutions.drilldown.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 查询数据范围
 * 
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-12 14:01
 */
@Getter
@AllArgsConstructor
public enum StatisticQueryByEnum {
    Order(1, "订单"), 
    TodayCustomer(2, "今日客户"), 
    PublicSeaFollow(3, "公海跟进"), 
    StockCustomer(4, "存量客户"), 
    TodayPublicSea(5, "今日投入公海"), 
    StockPublicSea(6, "存量投入公海"), 
    TodayToDo(7, "今日待办"), 
    AuntInterview(8, "阿姨约面"),
    n3Task(9, "新3任务"),

    ;

    private final int code;
    private final String name;

    public static StatisticQueryByEnum getEnumByCode(Integer code) {
        for (StatisticQueryByEnum obj : StatisticQueryByEnum.values()) {
            if (Objects.equals(obj.getCode(), code)) {
                return obj;
            }
        }
        return null;
    }

}
