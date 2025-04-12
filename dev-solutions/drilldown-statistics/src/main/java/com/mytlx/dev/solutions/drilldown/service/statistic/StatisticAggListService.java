package com.mytlx.dev.solutions.drilldown.service.statistic;

import com.mytlx.arcane.utils.Lang;
import com.mytlx.dev.solutions.drilldown.entity.User;
import com.mytlx.dev.solutions.drilldown.enums.*;
import com.mytlx.dev.solutions.drilldown.router.In;
import com.mytlx.dev.solutions.drilldown.router.Out;
import com.mytlx.dev.solutions.drilldown.router.Response;
import com.mytlx.dev.solutions.drilldown.service.calculator.Calculators;
import com.mytlx.dev.solutions.drilldown.service.city.CityService;
import com.mytlx.dev.solutions.drilldown.service.company.CompanyService;
import com.mytlx.dev.solutions.drilldown.service.user.UserService;
import com.mytlx.dev.solutions.drilldown.vo.StatisticAggVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-12 15:07
 */
@Slf4j
@Component
public class StatisticAggListService {

    @Autowired
    private Calculators calc;
    @Autowired
    private UserService userService;
    @Autowired
    private CityService cityService;
    @Autowired
    private CompanyService companyService;

    public Out platformAggList(In in) throws Exception {

        var user = userService.getPlatformUser(in.getPlatformUserId());
        var groupBy = guessGroupBy(in, user);
        List<Object> rangeIds = guessRangeIds(in, groupBy, user);
        Long companyId = null;
        if (groupBy == StatisticGroupByEnum.Agent) {
            companyId = in.getLong("rangeId");
        }
        return aggList(in, groupBy, companyId, null, rangeIds);
    }

    public Out appAggList(In in) throws Exception {

        var user = userService.getLoginUser(in.getLoginUserId());
        if (user == null) {
            return Out.json(Response.failMsg("无操作权限，请联系管理员或店长"));
        }
        List<Object> rangeIds = guessRangeIds(user);
        return aggList(in, StatisticGroupByEnum.Agent, user.getCompanyId(), user.getShopId(), rangeIds);
    }

    public Out aggList(In in, StatisticGroupByEnum groupBy, Long companyId, Long shopId, List<Object> rangeIds) throws Exception {

        int queryBy = in.getInteger("queryBy", 0);
        // 根据查询类型初步组装返回数据列表
        // 初始化行信息，后续对每行信息中缺少的列数据进行补充
        List<StatisticAggVO.AggItem> aggItems = new ArrayList<>();
        if (groupBy == StatisticGroupByEnum.BigArea) {
            aggItems = Arrays.stream(BigAreaEnum.values())
                    .map(k -> {
                        var aggItem = new StatisticAggVO.AggItem();
                        aggItem.setShowType(0);
                        aggItem.setRangeId(k.getCode() + "");
                        aggItem.setRangeBy(StatisticRangeByEnum.BigArea.getCode());
                        aggItem.setRangeName(k.getName());
                        return aggItem;
                    }).collect(Collectors.toList());
        } else if (groupBy == StatisticGroupByEnum.City) {
            Out.assertTrue(!rangeIds.isEmpty(), 1, "城市列表为空");
            var cityList = cityService.getListByIds(rangeIds);
            aggItems = cityList.stream().map(city -> {
                var aggItem = new StatisticAggVO.AggItem();
                aggItem.setShowType(0);
                aggItem.setRangeId(city.getId() + "");
                aggItem.setRangeBy(StatisticRangeByEnum.City.getCode());
                aggItem.setRangeName(city.getName());
                return aggItem;
            }).collect(Collectors.toList());
        } else if (groupBy == StatisticGroupByEnum.Company) {
            Out.assertTrue(!rangeIds.isEmpty(), 1, "公司列表为空");
            aggItems = companyService.getListByIds(rangeIds).stream()
                    .map(company -> {
                        var aggItem = new StatisticAggVO.AggItem();
                        aggItem.setShowType(0);
                        aggItem.setCompanyId(company.getId() + "");
                        aggItem.setRangeId(company.getId() + "");
                        aggItem.setRangeBy(StatisticRangeByEnum.Company.getCode());
                        aggItem.setRangeName(company.getName());
                        return aggItem;
                    }).collect(Collectors.toList());
        } else if (groupBy == StatisticGroupByEnum.Agent) {
            Out.assertTrue(!rangeIds.isEmpty(), 1, "经纪人列表为空");
            aggItems.addAll(userService.getListByIds(rangeIds).stream()
                    .map(agent -> {
                        var aggItem = new StatisticAggVO.AggItem();
                        aggItem.setShowType(0);
                        aggItem.setStatus(agent.getStatus());
                        aggItem.setCompanyId(agent.getCompanyId() + "");
                        aggItem.setShopId(agent.getShopId() + "");
                        aggItem.setRangeId(agent.getId() + "");
                        aggItem.setRangeBy(StatisticRangeByEnum.Agent.getCode());
                        aggItem.setRangeName(agent.getName());
                        return aggItem;
                    }).toList()
            );

            if (queryBy == 2 || queryBy == 1 || queryBy == 9) {
                boolean showUnAssign = true;
                // 判断是否有分配权限
                if (showUnAssign) {
                    var aggItem = new StatisticAggVO.AggItem();
                    aggItem.setShowType(3);
                    aggItem.setCompanyId(companyId + "");
                    aggItem.setRangeId("0");
                    aggItem.setRangeBy(StatisticRangeByEnum.Agent.getCode());
                    aggItem.setRangeName("未分配");
                    aggItems.add(aggItem);
                    rangeIds.add(0L);
                }
            }
        }
        // 根据当前查询列表页面补充其它数据
        // 按列进行查询，每次查询一列，将列信息补充到上面初始化的行信息中
        if (queryBy == StatisticQueryByEnum.Order.getCode()) { // 订单
            var achieveOrderSignMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Achieve_OrderSign);
            var achieveOrderRefundMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Achieve_OrderRefund);
            var achieveOrderAmountMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Achieve_OrderAmount);
            var achieveRevenueFinanceMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Achieve_RevenueFinance);
            var customerMonthAssignMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Customer_Month_Assign);

            aggItems.forEach(aggItem -> {
                aggItem.setAchieveOrderSign(achieveOrderSignMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setAchieveOrderRefund(achieveOrderRefundMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setAchieveOrderAmountRaw(achieveOrderAmountMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setAchieveRevenueFinanceRaw(achieveRevenueFinanceMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setCustomerMonthAssign(customerMonthAssignMap.get().getOrDefault(aggItem.getRangeId(), 0L));
            });
        } else if (queryBy == StatisticQueryByEnum.TodayCustomer.getCode()) { // 今日客户
            var customerTodayPublicSeaMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Customer_Today_PublicSea);
            var customerTodayAssignMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Customer_Today_Assign);
            var customerTodaySignMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Customer_Today_Sign);
            var customerTodayAMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Customer_Today_A);
            var customerTodayBMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Customer_Today_B);
            var customerTodayInterviewMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Customer_Today_Interview);
            aggItems.forEach(aggItem -> {
                if (groupBy == StatisticGroupByEnum.Agent) {
                    aggItem.setCustomerTodayAssign(customerTodayAssignMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                } else {
                    aggItem.setCustomerTodayAssign(customerTodayAssignMap.get().getOrDefault(aggItem.getRangeId(), 0L)
                            + customerTodayPublicSeaMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                }
                aggItem.setCustomerTodaySign(customerTodaySignMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setCustomerTodayA(customerTodayAMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setCustomerTodayB(customerTodayBMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setCustomerTodayInterview(customerTodayInterviewMap.get().getOrDefault(aggItem.getRangeId(), 0L));
            });
        } else if (queryBy == StatisticQueryByEnum.PublicSeaFollow.getCode()) { // 公海跟进
            var publicSeaFollowMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.PublicSea_Follow);
            var publicSeaSignMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.PublicSea_Sign);
            var publicSeaInterviewMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.PublicSea_Interview);
            var publicSeaAMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.PublicSea_A);
            var publicSeaBMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.PublicSea_B);
            aggItems.forEach(aggItem -> {
                aggItem.setPublicSeaFollow(publicSeaFollowMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setPublicSeaFollowSign(publicSeaSignMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setPublicSeaFollowInterview(publicSeaInterviewMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setPublicSeaFollowA(publicSeaAMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setPublicSeaFollowB(publicSeaBMap.get().getOrDefault(aggItem.getRangeId(), 0L));
            });
        } else if (queryBy == StatisticQueryByEnum.StockCustomer.getCode()) { // 存量客户
            var customerAllMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Customer_All);
            var customerAllAMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Customer_All_A);
            var customerAllBMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Customer_All_B);
            var customerAllInterviewMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Customer_All_Interview);
            var customerAllTomorrowInterviewMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Customer_All_Tomorrow_Interview);
            var customerAllToPayMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Customer_All_ToPay);
            var customerAllTodayVideoInterviewedMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Customer_All_TodayVideoInterviewed);
            aggItems.forEach(aggItem -> {
                aggItem.setCustomerAll(customerAllMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setCustomerAllA(customerAllAMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setCustomerAllB(customerAllBMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setCustomerAllInterview(customerAllInterviewMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setCustomerAllTomorrowInterview(customerAllTomorrowInterviewMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setCustomerAllToPay(customerAllToPayMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setCustomerAllTodayVideoInterviewed(customerAllTodayVideoInterviewedMap.get().getOrDefault(aggItem.getRangeId(), 0L));
            });
        } else if (queryBy == StatisticQueryByEnum.TodayPublicSea.getCode()) { // 今日投入公海
            var customerTodayPublicSeaMap = calc.calcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Customer_Today_PublicSea);
            aggItems.forEach(aggItem -> {
                aggItem.setCustomerTodayPublicSea(customerTodayPublicSeaMap.getOrDefault(aggItem.getRangeId(), 0L));
            });
        } else if (queryBy == StatisticQueryByEnum.StockPublicSea.getCode()) { // 存量投入存量
            var customerAllPublicSeaMap = calc.calcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Customer_All_PublicSea);
            aggItems.forEach(aggItem -> {
                aggItem.setCustomerAllPublicSea(customerAllPublicSeaMap.getOrDefault(aggItem.getRangeId(), 0L));
            });
        } else if (queryBy == StatisticQueryByEnum.TodayToDo.getCode()) { // 今日待办
            var todoPayMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Todo_Pay);
            var customerToSignMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Todo_CustomerToSign);
            var auntToSignMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Todo_AuntToSign);
            var renewMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Todo_Renew);
            aggItems.forEach(aggItem -> {
                aggItem.setTodoPay(todoPayMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setTodoCustomerToSign(customerToSignMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setTodoAuntToSign(auntToSignMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setTodoRenew(renewMap.get().getOrDefault(aggItem.getRangeId(), 0L));
            });
        } else if (queryBy == StatisticQueryByEnum.AuntInterview.getCode()) { // 阿姨约面
            var toInterviewMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Interview_Today_ToVideoInterview);
            var toFeedbackMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.Interview_Today_ToFeedback);
            aggItems.forEach(aggItem -> {
                aggItem.setInterviewTodayToVideoInterview(toInterviewMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setInterviewTodayToFeedback(toFeedbackMap.get().getOrDefault(aggItem.getRangeId(), 0L));
            });
        } else if (queryBy == StatisticQueryByEnum.n3Task.getCode()) { // n3 员工待办
            var notCoverMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.N3_Task_Today_Not_Cover);
            var connectFailMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.N3_Task_Connect_Fail_Lt3);
            var notFollowMap = calc.asyncCalcGroup(in, groupBy, companyId, shopId, rangeIds, CalculatorItem.N3_Task_N3_Not_Follow);
            aggItems.forEach(aggItem -> {
                aggItem.setN3TaskTodayNotCover(notCoverMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setN3TaskConnectFailLt3(connectFailMap.get().getOrDefault(aggItem.getRangeId(), 0L));
                aggItem.setN3TaskTodayNotFollow(notFollowMap.get().getOrDefault(aggItem.getRangeId(), 0L));
            });
        }

        // 排序
        String rawSortBy = in.getString("sortBy", "rangeId");
        String realSortBy = rawSortBy;
        if (rawSortBy.equals("achieveOrderAmount")) {
            realSortBy = "achieveOrderAmountRaw";
        } else if (rawSortBy.equals("achieveRevenueFinance")) {
            realSortBy = "achieveRevenueFinanceRaw";
        }
        try {
            Lang.get(new StatisticAggVO.AggItem(), realSortBy);
        } catch (Exception e) {
            throw new IllegalArgumentException("illegal sortBy = " + realSortBy);
        }
        String order = in.getString("order", "desc");
        String finalRealSortBy = realSortBy;
        aggItems = aggItems.stream()
                .sorted((o1, o2) -> {
                    // 未分配的固定在顶部
                    if (o1.getRangeId().equals("0")) {
                        return -1;
                    }
                    if (o2.getRangeId().equals("0")) {
                        return 1;
                    }
                    long compare = 0;
                    try {
                        Object v1 = Lang.get(o1, finalRealSortBy);
                        Object v2 = Lang.get(o2, finalRealSortBy);
                        if (v1 == null) return 1;
                        if (v2 == null) return -1;
                        if (v1 instanceof Long) {
                            compare = (Long) v2 - (Long) v1;
                        } else if (v1 instanceof String) {
                            if (((String) v1).endsWith("%")) {
                                compare = (long) (Double.parseDouble(((String) v2).replace("%", "")) * 10000.0
                                        - Double.parseDouble(((String) v1).replace("%", "")) * 10000.0);
                            }
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                    return (int) (order.equals("desc") ? compare : -compare);
                })
                .filter(k -> {
                    // 隐藏未分配
                    return !k.getRangeId().equals("0") || k.hasAnyData();
                })
                .collect(Collectors.toList());

        // 今日投入公海
        if (groupBy == StatisticGroupByEnum.Agent && queryBy == StatisticQueryByEnum.TodayCustomer.getCode()) {
            List<Object> agentIds = aggItems.stream().map(k -> Long.valueOf(k.getRangeId())).collect(Collectors.toList());
            var todayToSea = new StatisticAggVO.AggItem();
            todayToSea.setShowType(2);
            todayToSea.setCompanyId(companyId + "");
            todayToSea.setRangeId("");
            todayToSea.setRangeBy(aggItems.getFirst().getRangeBy());
            todayToSea.setRangeName("投入公海");
            if (queryBy == 2) {
                todayToSea.setCustomerTodayPublicSea(
                        calc.calcTotal(in, StatisticRangeByEnum.Agent, companyId, shopId,
                                agentIds, CalculatorItem.Customer_Today_PublicSea)
                );
                todayToSea.setCustomerTodayAssign(todayToSea.getCustomerTodayPublicSea());
            } else if (queryBy == 4) {
                todayToSea.setCustomerTodayPublicSea(
                        calc.calcTotal(in, StatisticRangeByEnum.Agent, companyId, shopId,
                                agentIds, CalculatorItem.Customer_All_PublicSea)
                );
            }
            aggItems.addFirst(todayToSea);
        }
        // 汇总
        insertTotalLine(in, groupBy, companyId, shopId, aggItems);

        aggItems = aggItems.stream()
                .filter(k -> {
                    // 统计业绩时隐藏未分配的月分配客户量
                    return queryBy != StatisticQueryByEnum.Order.getCode() || !Objects.equals(k.getRangeId(), "0");
                })
                .collect(Collectors.toList());
        StatisticAggVO vo = new StatisticAggVO();
        vo.setGroupBy(groupBy.getCode());
        vo.setRangeIds(rangeIds);
        vo.setAggItems(aggItems);
        return Out.json(Response.success(vo));
    }

    /**
     * 头部汇总行，第一行
     */
    private void insertTotalLine(In in, StatisticGroupByEnum rangeBy, Long companyId, Long shopId,
                                 List<StatisticAggVO.AggItem> aggItems) {
        if (aggItems.isEmpty()) return;

        var total = new StatisticAggVO.AggItem();
        total.setShowType(1);
        // 列表里的 rangeId 应该为 shopId 或者 companyId
        total.setRangeBy(rangeBy.getCode());
        if (rangeBy == StatisticGroupByEnum.Agent) { // 如果是按人分组，则 rangeId 为 shopId 或者 companyId
            total.setRangeId(in.getString("rangeId"));
        }
        total.setRangeName("汇总");
        total.setCompanyId(companyId == null ? "" : companyId.toString());
        total.setShopId(shopId == null ? "" : shopId.toString());

        total.setTodoPay(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getTodoPay).sum());
        total.setTodoCustomerToSign(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getTodoCustomerToSign).sum());
        total.setTodoAuntToSign(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getTodoAuntToSign).sum());
        total.setTodoRenew(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getTodoRenew).sum());

        total.setAchieveOrderSign(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getAchieveOrderSign).sum());
        total.setAchieveOrderRefund(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getAchieveOrderRefund).sum());
        total.setAchieveOrderAmountRaw(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getAchieveOrderAmountRaw).sum());
        total.setAchieveRevenueFinanceRaw(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getAchieveRevenueFinanceRaw).sum());

        total.setCustomerTodayAssign(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getCustomerTodayAssign).sum());
        total.setCustomerTodayA(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getCustomerTodayA).sum());
        total.setCustomerTodayB(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getCustomerTodayB).sum());
        total.setCustomerTodayInterview(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getCustomerTodayInterview).sum());
        total.setCustomerTodaySign(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getCustomerTodaySign).sum());
        total.setCustomerTodayPublicSea(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getCustomerTodayPublicSea).sum());

        total.setCustomerMonthAssign(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getCustomerMonthAssign).sum());

        total.setCustomerAll(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getCustomerAll).sum());
        total.setCustomerAllA(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getCustomerAllA).sum());
        total.setCustomerAllB(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getCustomerAllB).sum());
        total.setCustomerAllAB(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getCustomerAllAB).sum());
        total.setCustomerAllInterview(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getCustomerAllInterview).sum());
        total.setCustomerAllTomorrowInterview(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getCustomerAllTomorrowInterview).sum());
        total.setCustomerAllTodayVideoInterviewed(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getCustomerAllTodayVideoInterviewed).sum());
        total.setCustomerAllToPay(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getCustomerAllToPay).sum());
        total.setCustomerAllPublicSea(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getCustomerAllPublicSea).sum());

        total.setPublicSeaFollow(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getPublicSeaFollow).sum());
        total.setPublicSeaFollowA(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getPublicSeaFollowA).sum());
        total.setPublicSeaFollowB(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getPublicSeaFollowB).sum());
        total.setPublicSeaFollowInterview(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getPublicSeaFollowInterview).sum());
        total.setPublicSeaFollowSign(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getPublicSeaFollowSign).sum());

        total.setInterviewTodayToVideoInterview(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getInterviewTodayToVideoInterview).sum());
        total.setInterviewTodayToFeedback(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getInterviewTodayToFeedback).sum());

        total.setN3TaskTodayNotCover(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getN3TaskTodayNotCover).sum());
        total.setN3TaskConnectFailLt3(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getN3TaskConnectFailLt3).sum());
        total.setN3TaskTodayNotFollow(aggItems.stream().mapToLong(StatisticAggVO.AggItem::getN3TaskTodayNotFollow).sum());

        aggItems.addFirst(total);
    }

    /**
     * 根据传入的参数，判断需要查询的rangeIds是什么
     * 四类：大区id列表，城市id列表，公司id列表，经纪人id列表
     * 其实就是表格中的行头
     */
    @SneakyThrows
    public List<Object> guessRangeIds(In in, StatisticGroupByEnum groupBy, User user) {

        Long rangeId = in.getLong("rangeId");
        if (rangeId != null && rangeId > 0) {
            if (groupBy == StatisticGroupByEnum.City) { // 入参为大区 id
                // TODO: 判断是否有管理员权限，有则根据大区id（rangId）查询所有城市信息

                return new ArrayList<>();

                // Out.assertTrue(user.getRoleType() == OPERATOR_ROLE_MANAGER.getCode(), 1, "无权限");
                // return cityService.getListByPage("bigarea=?", 1, 500, new Object[]{ BigAreaEnum.getNameByCode(rangeId.intValue()) })
                //         .stream().map(k -> k.getDispLocalId()).collect(Collectors.toList());
            }
            if (groupBy == StatisticGroupByEnum.Company) { // 入参为城市 id
                // TODO: 判断是否有该城市权限，有则根据城市id查询所有公司id并返回
                List<Object> companyIds = new ArrayList<>();
                return companyIds;

                // Out.assertTrue(in.hasMeiShiAuthority(Castors.cast(Integer.class, rangeId)), 1, "无权限");
                // Sqls sql = Sqls.create().conEql("saas_settled", 110702)
                //         //                        .conEql("clear_status", 1) // 1-正常 2-暂停合作 3-待清退
                //         .conEql("city_id", rangeId)
                //         .conNotIn("id", maskComp.getMaskCompanyIds(in.getBspID()));
                // return companyDao.getListByPage(sql.getCondition(), 1, 500, sql.getParams()).stream().map(k -> k.getId()).collect(Collectors.toList());
            }
            if (groupBy == StatisticGroupByEnum.Agent) { // 入参为公司 id
                // TODO: 根据公司id查询公司是否存在，存在则判断是否有城市权限，有则根据公司id查询所有userId
                List<Object> userIds = new ArrayList<>();
                return userIds;

                // var company = companyDao.getEntityById(rangeId);
                // Out.assertNotNull(company, 1, "公司不存在");
                // Out.assertTrue(in.hasMeiShiAuthority(company.getCityId()), 1, "无权限");
                // return userDao.getListByPage("company_Id=?", 1, 500, new Object[]{ rangeId })
                //         .stream().map(k -> k.getId()).collect(Collectors.toList());
            }
        }

        if (user.getRoleType() == 2001) { // 全国权限，返回所有大区id列表
            return Arrays.stream(BigAreaEnum.values()).map(BigAreaEnum::getCode).collect(Collectors.toList());
        } else { // 单城市时按公司聚合，多城市时按城市聚合
            String[] cityIds = user.getCityIds().split("[,， ]+");
            if (cityIds.length == 0) {
                Out.abort(2, "无权限，请联系系统管理员");
            }
            if (cityIds.length == 1) { // 返回公司列表

                // TODO: 根据城市id查询所有公司id并返回
                return new ArrayList<>();

                // Sqls sql = Sqls.create().conEql("saas_settled", 110702)
                //         .conEql("clear_status", 1).conEql("city_id", cityIds[0])
                //         .conNotIn("id", maskComp.getMaskCompanyIds(in.getBspID()));
                // return companyDao.getListByPage(sql.getCondition(), 1, 500, sql.getParams()).stream().map(k -> k.getId()).collect(Collectors.toList());
            }
            return Arrays.asList(cityIds);
        }
    }

    /**
     * 店长查询门店所有员工，管理员查询公司所有员工
     */
    @SneakyThrows
    public List<Object> guessRangeIds(User user) {
        // TODO: 根据user的shopId和companyId查询user表中所有的userId
        List<Object> list = new ArrayList<>();
        list.add(236749192302592L);
        list.add(236749192302593L);
        list.add(236749192302594L);
        list.add(236749192302595L);
        list.add(236749192302596L);
        list.add(236749192302597L);
        list.add(236749192302598L);
        list.add(236749192302599L);
        list.add(236749192302600L);
        list.add(236749192302601L);
        return list;
    }

    /**
     * 判断数据聚合维度
     */
    @SneakyThrows
    public StatisticGroupByEnum guessGroupBy(In in, User user) {

        Integer rangeBy = in.getInteger("rangeBy");
        if (rangeBy != null && rangeBy > 0) {
            StatisticRangeByEnum result = StatisticRangeByEnum.getEnumByCode(rangeBy);
            Out.assertTrue(result != null, 1, "illegal rangeBy = " + rangeBy);
            switch (result) {
                case BigArea:
                    return StatisticGroupByEnum.City;
                case City:
                    return StatisticGroupByEnum.Company;
                case Company:
                    return StatisticGroupByEnum.Agent;
                default:
                    Out.abort(1, "illegal rangeBy = " + rangeBy);
            }
        }

        if (user.getRoleType() == 2001) { // 全国权限按大区聚合
            return StatisticGroupByEnum.BigArea;
        } else { // 单城市时按公司聚合，多城市时按城市聚合
            return user.getCityIds().split("[,， ]+").length > 1 ?
                    StatisticGroupByEnum.City : StatisticGroupByEnum.Company;
        }
    }
}
