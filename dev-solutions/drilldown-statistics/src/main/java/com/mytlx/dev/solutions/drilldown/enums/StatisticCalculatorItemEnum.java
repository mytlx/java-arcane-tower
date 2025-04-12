package com.mytlx.dev.solutions.drilldown.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 数据统计数据项
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-11 20:45
 */
@Getter
@AllArgsConstructor
public enum StatisticCalculatorItemEnum {

    Customer_Today_Assign(11, "今日分配"), // 今日分配
    Customer_Today_AB(12, "今日AB"), // 今日 AB
    Customer_Today_A(13, "今日A"), // 今日 A
    Customer_Today_B(14, "今日B"), // 今日 B
    Customer_Today_Interview(15, "今日约面"), // 今日约面
    Customer_Today_Sign(16, "今日签单"), // 今日签单

    Customer_Today_PublicSea(21, "今日投入公海"), // 今日投入公海

    Customer_All_AB(31, "累计客户AB"), // 累计 AB
    Customer_All_A(32, "累计客户A"), // 累计 A
    Customer_All_B(33, "累计客户B"), // 累计 B
    Customer_All_Interview(34, "累计约面"), // 累计约面
    Customer_All_Tomorrow_Interview(35, "累计明日约面"), // 明日约面
    Customer_All_ToPay(36, "累计待支付"), // 待支付
    Customer_All_TodayVideoInterviewed(55, "累计今日已面试"), // 今日已面试

    Customer_All_PublicSea(37, "累计投入公海"), // 投入公海

    PublicSea_Follow(41, "跟进线索"), // 公海跟进
    PublicSea_AB(42, "客户质量：A或B"), // 公海 AB
    PublicSea_A(43, "客户质量：A"), // 公海 A
    PublicSea_B(44, "客户质量：B"), // 公海 B
    PublicSea_Interview(45, "今日约面"), // 公海约面
    PublicSea_Sign(46, "当日签单"), // 公海当日签单

    Achieve_OrderSign(51, "签单"), // 签单，支持到公司
    Achieve_OrderRefund(52, "退单"), // 退单，支持到公司
    Achieve_OrderAmount(53, "营收（业务）"), // 成单金额，支持到公司
    Achieve_RevenueFinance(56, "营收（财务）"),

    Achieve_OrderRate(54, "转化"), // 转化，CalculatorItem 没有同名类

    Interview_Today_ToVideoInterview(57, "待视频面试"),
    Interview_Today_ToFeedback(58, "待反馈面试结果"),

    N3_Task_Today_Not_Cover(60, "今日未覆盖"), // 新3-今日未覆盖
    N3_Task_Connect_Fail_Lt3(61, "未接通拨打<3"), // 新3-电话未接通次数小于3
    N3_Task_N3_Not_Follow(62, "新3今日未跟进"), // 新3-今日未跟进

    ;

    private final int code;
    private final String name;

    public String getName() {
        if(this == PublicSea_Interview) {
            return "创建面试时间：" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        } else {
            return this.name;
        }
    }

    public static StatisticCalculatorItemEnum getEnumByCode(Integer code) {
        for (StatisticCalculatorItemEnum obj : StatisticCalculatorItemEnum.values()) {
            if (Objects.equals(obj.getCode(), code)) {
                return obj;
            }
        }
        return null;
    }

}
