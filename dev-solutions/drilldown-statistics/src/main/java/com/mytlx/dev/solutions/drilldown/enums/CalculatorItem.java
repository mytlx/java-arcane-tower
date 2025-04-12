package com.mytlx.dev.solutions.drilldown.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CalculatorItem {

    Todo_Pay(0), // 待支付单，支持到公司
    Todo_CustomerToSign(0), // 客户待签，支持到公司
    Todo_AuntToSign(0), // 阿姨待签，支持到公司
    Todo_Renew(0), //待续约，支持到公司
    Achieve_OrderSign(0), // 签单，支持到公司
    Achieve_OrderRefund(0), // 退单，支持到公司
    Achieve_OrderAmount(0), // 成单金额，支持到公司
    Achieve_RevenueFinance(0),// 营收（财务）

    Customer_Today_Assign(1), // 今日分配
    Customer_Today_AB(1), // 今日 AB
    Customer_Today_A(1), // 今日 A
    Customer_Today_B(1), // 今日 B
    Customer_Today_Interview(1), // 今日约面
    Customer_Today_Sign(1), // 今日签单
    Customer_Today_PublicSea(1), // 今日投入公海

    Customer_Month_Assign(1), // 当月分配

    Customer_All(1), // 累计
    Customer_All_AB(1), // 累计 AB
    Customer_All_A(1), // 累计 A
    Customer_All_B(1), // 累计 B
    Customer_All_Interview(1), // 累计约面
    Customer_All_Tomorrow_Interview(1), // 明日约面
    Customer_All_ToPay(1), // 待支付
    Customer_All_PublicSea(1), // 投入公海
    Customer_All_TodayVideoInterviewed(1), // 已约面

    PublicSea_Follow(2), // 公海跟进
    PublicSea_AB(2), // 公海 AB
    PublicSea_A(2), // 公海 A
    PublicSea_B(2), // 公海 B
    PublicSea_Interview(2), // 公海约面
    PublicSea_Sign(2), // 公海当日签单

    Interview_Today_ToVideoInterview(3), // 视频面试 待视频
    Interview_Today_ToFeedback(3), // 待反馈面试结果

    N3_Task_Today_Not_Cover(4), // 新3-今日未覆盖
    N3_Task_Connect_Fail_Lt3(4), // 新3-电话未接通次数小于3
    N3_Task_N3_Not_Follow(4), // 新3-今日未跟进
    ;

    private final int dsType; // 数据源类型， 0 订单，1客户, 2公海跟进, 3计算，4新三

    public String getKey(StatisticRangeByEnum range, Object id) {

        return "Calculator2-" + range.name() + "-" + this.name() + "-" + id;
    }

    public int getDsType() {
        return dsType;
    }


}
