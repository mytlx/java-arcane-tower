package com.mytlx.dev.solutions.drilldown.service.statistic;

import com.mytlx.arcane.utils.Async;
import com.mytlx.dev.solutions.drilldown.enums.BigAreaEnum;
import com.mytlx.dev.solutions.drilldown.enums.CalculatorItem;
import com.mytlx.dev.solutions.drilldown.enums.StatisticRangeByEnum;
import com.mytlx.dev.solutions.drilldown.router.In;
import com.mytlx.dev.solutions.drilldown.router.Out;
import com.mytlx.dev.solutions.drilldown.router.Response;
import com.mytlx.dev.solutions.drilldown.service.calculator.Calculators;
import com.mytlx.dev.solutions.drilldown.service.user.UserService;
import com.mytlx.dev.solutions.drilldown.vo.StatisticDashboardVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-12 21:39
 */
@Service
@Slf4j
public class DashboardService {

    @Autowired
    private UserService userService;
    @Autowired
    private Calculators calc;

    public Out dashboard(In in) throws Exception {

        var user = userService.getPlatformUser(in.getPlatformUserId());
        StatisticDashboardVO result = new StatisticDashboardVO();
        if(user.getRoleType() == 2001) { // 全国权限
            List<Object> areaIds = Arrays.stream(BigAreaEnum.values())
                    .map(BigAreaEnum::getCode)
                    .collect(Collectors.toList());
            result.setManagementGroups(
                    Async.collectSkipNull(
                            () -> n3Task(in, StatisticRangeByEnum.BigArea, areaIds),// 员工待办
                            () -> achieveGroup(in, StatisticRangeByEnum.BigArea, areaIds),// 本月业绩
                            () -> customerToday(in, StatisticRangeByEnum.BigArea, areaIds),// 今日跟进
                            () -> publicSea(in, StatisticRangeByEnum.BigArea, areaIds),// 公海跟进
                            () -> customerAll(in, StatisticRangeByEnum.BigArea, areaIds)// 积累客户
                    )
            );
        } else {
            List<Object> cityIds = Arrays.asList(user.getCityIds().split("[,， ]+"));
            if(cityIds == null || cityIds.size() == 0) {
                return Out.json(Response.failMsg("请刷新页面"));
            }
            result.setManagementGroups(
                    Async.collectSkipNull(
                            () -> n3Task(in, StatisticRangeByEnum.City, cityIds),// 员工待办
                            () -> achieveGroup(in, StatisticRangeByEnum.City, cityIds),// 本月业绩
                            () -> customerToday(in, StatisticRangeByEnum.City, cityIds),// 今日跟进
                            () -> publicSea(in, StatisticRangeByEnum.City, cityIds),// 公海跟进
                            () -> customerAll(in, StatisticRangeByEnum.City, cityIds)// 积累客户
                    )
            );
        }

        return Out.json(Response.success(result));
    }

    public StatisticDashboardVO.ManagementGroup customerAll(In in, StatisticRangeByEnum rangeBy, List<Object> rangeIds) {

        var group = new StatisticDashboardVO.ManagementGroup();
        group.setManagementCodeName("CustomerAll");
        group.setManagementTitle("积累客户");
        group.setPopupText("AB线索：未签单线索AB线索总量（包含自建）\n\n" +
                "约面：未签单线索已约面试客户总量\n\n" +
                "明日约面：预约面试时间为明日的客户数量\n\n" +
                "视频面试：今日通过视频面试客户");
        group.setManagementItems(
                Async.collectSkipNull(
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(Customer_All_AB.getCode());
                            // item.setManagementCodeName(Customer_All_AB.name());
                            item.setManagementKey("AB线索");
                            item.setManagementValue(
                                    calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.Customer_All_AB) + ""
                            );
                            return item;
                        },
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(Customer_All_Interview.getCode());
                            // item.setManagementCodeName(Customer_All_Interview.name());
                            item.setManagementKey("约面");
                            item.setManagementValue(
                                    calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.Customer_All_Interview) + ""
                            );
                            return item;
                        },
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(Customer_All_TodayVideoInterviewed.getCode());
                            // item.setManagementCodeName(Customer_All_TodayVideoInterviewed.name());
                            item.setManagementKey("视频面试");
                            item.setManagementValue(
                                    calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.Customer_All_TodayVideoInterviewed) + ""
                            );
                            return item;
                        },
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(Customer_All_Tomorrow_Interview.getCode());
                            // item.setManagementCodeName(Customer_All_Tomorrow_Interview.name());
                            item.setManagementKey("明日约面");
                            item.setManagementValue(
                                    calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.Customer_All_Tomorrow_Interview) + ""
                            );
                            return item;
                        }
                )
        );
        return group;
    }

    public StatisticDashboardVO.ManagementGroup publicSea(In in, StatisticRangeByEnum rangeBy, List<Object> rangeIds) {

        var group = new StatisticDashboardVO.ManagementGroup();
        group.setManagementCodeName("PublicSea");
        group.setManagementTitle("公海线索-今日跟进");
        group.setPopupText("跟进线索：今日从公海捞取的线索，并且创建了跟进记录\n\n" +
                "AB线索：今日从公海捞取的线索，创建了跟进记录，并且客户质量在跟进时被标记AB\n\n" +
                "约面：今日从公海捞取的线索，今日创建约面记录\n\n" +
                "当日签单：今日从公海捞取的线索，当日成单");
        group.setManagementItems(
                Async.collectSkipNull(
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(PublicSea_Follow.getCode());
                            // item.setManagementCodeName(PublicSea_Follow.name());
                            item.setManagementKey("跟进线索");
                            item.setManagementValue(
                                    calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.PublicSea_Follow) + ""
                            );
                            return item;
                        },
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(PublicSea_AB.getCode());
                            // item.setManagementCodeName(PublicSea_AB.name());
                            item.setManagementKey("AB线索");
                            item.setManagementValue(
                                    calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.PublicSea_AB) + ""
                            );
                            return item;
                        },
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(PublicSea_Interview.getCode());
                            // item.setManagementCodeName(PublicSea_Interview.name());
                            item.setManagementKey("约面");
                            item.setManagementValue(
                                    calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.PublicSea_Interview) + ""
                            );
                            return item;
                        },
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(PublicSea_Sign.getCode());
                            // item.setManagementCodeName(PublicSea_Sign.name());
                            item.setManagementKey("今日签单");
                            item.setManagementValue(
                                    calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.PublicSea_Sign) + ""
                            );
                            return item;
                        }
                )
        );
        return group;
    }

    public StatisticDashboardVO.ManagementGroup customerToday(In in, StatisticRangeByEnum rangeBy, List<Object> rangeIds) {

        var group = new StatisticDashboardVO.ManagementGroup();
        group.setManagementCodeName("CustomerToday");
        group.setManagementTitle("今日线索-今日跟进");
        group.setPopupText("分配线索：创建时间为今日的平台线索\n\n" +
                "AB线索：创建时间为今日的平台线索，标记线索质量为AB的客户\n\n" +
                "约面：创建时间为今日的平台线索，添加了约面记录\n\n" +
                "当日签单：今日的平台线索今日成交");
        group.setManagementItems(
                Async.collectSkipNull(
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(Customer_Today_Assign.getCode());
                            // item.setManagementCodeName(Customer_Today_Assign.name());
                            item.setManagementKey("分配线索");
                            Long totalAssign = calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.Customer_Today_Assign);
                            Long totalPublicSea = calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.Customer_Today_PublicSea);
                            item.setManagementValue(Arrays.asList(totalAssign, totalPublicSea).stream()
                                    .filter(k -> k != null).mapToLong(Long::longValue).sum() + "");
                            return item;
                        },
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(Customer_Today_AB.getCode());
                            // item.setManagementCodeName(Customer_Today_AB.name());
                            item.setManagementKey("AB线索");
                            item.setManagementValue(
                                    calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.Customer_Today_AB) + ""
                            );
                            return item;
                        },
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(Customer_Today_Interview.getCode());
                            // item.setManagementCodeName(Customer_Today_Interview.name());
                            item.setManagementKey("约面");
                            item.setManagementValue(
                                    calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.Customer_Today_Interview) + ""
                            );
                            return item;
                        },
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(Customer_Today_Sign.getCode());
                            // item.setManagementCodeName(Customer_Today_Sign.name());
                            item.setManagementKey("今日签单");
                            item.setManagementValue(
                                    calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.Customer_Today_Sign) + ""
                            );
                            return item;
                        }
                )
        );
        return group;
    }

    public StatisticDashboardVO.ManagementGroup achieveGroup(In in, StatisticRangeByEnum rangeBy, List<Object> rangeIds) {
        var group = new StatisticDashboardVO.ManagementGroup();
        group.setManagementCodeName("Achieve");
        group.setManagementTitle("本月业绩");
        group.setPopupText("签单：支付时间为本月的订单数量\n\n" +
                "退单：退款成功时间为本月的订单数量\n\n" +
                "营收（业务）：保姆育儿嫂首次匹配费加售后服务费，月嫂单已支付部分的服务费\n\n" +
                "营收（财务）：保姆育儿嫂首次匹配费加售后服务费，月嫂支付尾款后的服务费（财务口径，月嫂只支付首款不计入营收）\n\n" +
                "转化率：支付时间为本月的订单数量/创建时间为本月的平台客户数量");
        group.setManagementItems(
                Async.collectSkipNull(
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(Achieve_OrderSign.getCode());
                            // item.setManagementCodeName(Achieve_OrderSign.name());
                            item.setManagementKey("签单");
                            item.setManagementValue(
                                    calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.Achieve_OrderSign) + ""
                            );
                            return item;
                        },
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(Achieve_OrderRefund.getCode());
                            // item.setManagementCodeName(Achieve_OrderRefund.name());
                            item.setManagementKey("退单");
                            item.setManagementValue(
                                    calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.Achieve_OrderRefund) + ""
                            );
                            return item;
                        },
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(Achieve_OrderAmount.getCode());
                            // item.setManagementCodeName(Achieve_OrderAmount.name());
                            item.setManagementKey("营收（业务）");
                            long raw = calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.Achieve_OrderAmount);
                            item.setManagementValue(
                                    raw >= 10000 ? (raw/10000) / 100.0 + "W" : raw/100.0 + ""
                            );
                            return item;
                        },
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(Achieve_RevenueFinance.getCode());
                            // item.setManagementCodeName(Achieve_RevenueFinance.name());
                            item.setManagementKey("营收（财务）");
                            long raw = calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.Achieve_RevenueFinance);
                            item.setManagementValue(
                                    raw >= 10000 ? (raw/10000) / 100.0 + "W" : raw/100.0 + ""
                            );
                            return item;
                        },
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(Achieve_OrderRate.getCode());
                            // item.setManagementCodeName(Achieve_OrderRate.name());
                            var orderSign = calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.Achieve_OrderSign);
                            var customerAssign = calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.Customer_Month_Assign);
                            item.setManagementKey("转化率");
                            if(orderSign > 0 && customerAssign > 0) {
                                item.setManagementValue(
                                        (orderSign * 100 * 100 / customerAssign) / 100.0 + "%"
                                );
                            } else {
                                item.setManagementValue("0%");
                            }
                            return item;
                        }
                )
        );
        return group;
    }

    public StatisticDashboardVO.ManagementGroup n3Task(In in, StatisticRangeByEnum rangeBy, List<Object> rangeIds) {
        var group = new StatisticDashboardVO.ManagementGroup();
        group.setManagementCodeName("N3Task");
        group.setManagementTitle("员工待办");
        group.setPopupText("今日未覆盖：未外呼的平台线索\n\n" +
                "未接通拨打<3：始终未接通客户，当日拨打小于3次\n\n" +
                "新3今日未跟进：3天内客户今日还未进行跟进\n\n");
        group.setManagementItems(
                Async.collectSkipNull(
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(N3_Task_Today_Not_Cover.getCode());
                            // item.setManagementCodeName(N3_Task_Today_Not_Cover.name());
                            item.setManagementKey("今日未覆盖");
                            item.setManagementValue(
                                    calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.N3_Task_Today_Not_Cover) + ""
                            );
                            return item;
                        },
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(N3_Task_Connect_Fail_Lt3.getCode());
                            // item.setManagementCodeName(N3_Task_Connect_Fail_Lt3.name());
                            item.setManagementKey("未接通拨打<3");
                            item.setManagementValue(
                                    calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.N3_Task_Connect_Fail_Lt3) + ""
                            );
                            return item;
                        },
                        () -> {
                            var item = new StatisticDashboardVO.ManagementItem();
                            // item.setManagementCode(N3_Task_N3_Not_Follow.getCode());
                            // item.setManagementCodeName(N3_Task_N3_Not_Follow.name());
                            item.setManagementKey("新3今日未跟进");
                            item.setManagementValue(
                                    calc.calcTotal(in, rangeBy, null, null, rangeIds, CalculatorItem.N3_Task_N3_Not_Follow) + ""
                            );
                            return item;
                        }
                )
        );
        return group;
    }

}
