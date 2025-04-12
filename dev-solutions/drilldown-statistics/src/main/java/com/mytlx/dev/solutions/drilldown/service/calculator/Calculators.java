package com.mytlx.dev.solutions.drilldown.service.calculator;

import com.mytlx.arcane.utils.Exec;
import com.mytlx.dev.solutions.drilldown.enums.CalculatorItem;
import com.mytlx.dev.solutions.drilldown.enums.StatisticGroupByEnum;
import com.mytlx.dev.solutions.drilldown.enums.StatisticRangeByEnum;
import com.mytlx.dev.solutions.drilldown.router.CrmPage;
import com.mytlx.dev.solutions.drilldown.router.In;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-12 14:10
 */
@Component
@Slf4j
public class Calculators implements ApplicationContextAware {

    private final ConcurrentHashMap<Integer, Calculator> calcMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        calcMap.put(0, applicationContext.getBean(OrderCalculator.class));
        calcMap.put(1, applicationContext.getBean(CustomerCalculator.class));
        calcMap.put(2, applicationContext.getBean(PublicSeaCalculator.class));
        calcMap.put(3, applicationContext.getBean(InterviewCalculator.class));
        calcMap.put(4, applicationContext.getBean(N3TaskCalculator.class));
    }

    public SafeFuture<Map<String, Long>> asyncCalcGroup(In in, StatisticGroupByEnum groupBy, Long companyId,
                                                        Long shopId, List<Object> groupIds, CalculatorItem item) throws Exception {
        return new SafeFuture<>(Exec.submit(() -> calcGroup(in, groupBy, companyId, shopId, groupIds, item)));
    }

    public Long calcTotal(StatisticRangeByEnum rangeBy, Long companyId, Long shopId, List<Object> rangeIds, CalculatorItem item) throws Exception {
        return calcTotal(new In(), rangeBy, companyId, shopId, rangeIds, item);
    }

    /**
     * @param rangeBy  限制范围，
     *                 大区、  城市、  公司
     * @param rangeIds 大区ID、城市ID、公司ID、经纪人ID
     */
    public Long calcTotal(In in, StatisticRangeByEnum rangeBy, Long companyId, Long shopId,
                          List<Object> rangeIds, CalculatorItem item) throws Exception {

        var result = calcGroup(in, rangeBy, companyId, shopId, rangeIds, item)
                .values().stream().filter(Objects::nonNull).mapToLong(Long::longValue).sum();
        log.debug("calcTotal rangeBy = {}, rangeIds = {}, item = {}, result = {}", rangeBy, rangeIds, item, result);
        return result;
    }

    public Map<String, Long> calcGroup(In in, StatisticRangeByEnum groupBy, Long companyId, Long shopId,
                                       List<Object> groupIds, CalculatorItem item) throws Exception {

        return calcGroup(in, StatisticGroupByEnum.getEnumByCode(groupBy.getCode()), companyId, shopId, groupIds, item);
    }

    /**
     * @param groupBy  分组方式，
     *                 大区、  城市、  公司、  经纪人
     * @param groupIds 大区ID、城市ID、公司ID、经纪人ID
     */
    public Map<String, Long> calcGroup(In in, StatisticGroupByEnum groupBy, Long companyId, Long shopId,
                                       List<Object> groupIds, CalculatorItem item) throws Exception {

        if (groupIds == null || groupIds.isEmpty()) {
            return new HashMap<>();
        }
        // 使用对应的calculator进行查询
        var calc = calcMap.get(item.getDsType());
        if (calc == null) {
            throw new IllegalArgumentException("unknown calc type = " + item);
        }
        Map<String, Long> result = null;
        if (groupBy == StatisticGroupByEnum.Agent) {
            result = calc.calcAgent(in, companyId, shopId, groupIds.stream().map(Long.class::cast).collect(Collectors.toList()), item);
        } else if (groupBy == StatisticGroupByEnum.Company) {
            result = calc.calcCompany(in, groupIds.stream().map(Long.class::cast).collect(Collectors.toList()), item);
        } else if (groupBy == StatisticGroupByEnum.City) {
            result = calc.calcCity(in, groupIds.stream().map(Integer.class::cast).collect(Collectors.toList()), item);
        } else if (groupBy == StatisticGroupByEnum.BigArea) {
            result = calc.calcBigArea(in, groupIds.stream().map(Integer.class::cast).collect(Collectors.toList()), item);
        } else {
            throw new IllegalArgumentException("illegal groupBy = " + groupBy);
        }
        log.debug("calcTotal groupBy = {}, groupIds = {}, item = {}, result = {}", groupBy, groupIds, item, result);
        return result == null ? new HashMap<>() : result;
    }

    public CrmPage<?> list(Long companyId, Long shopId, Long agentId, In in, CalculatorItem item) {

        var calc = calcMap.get(item.getDsType());
        if(calc == null) {
            throw new IllegalArgumentException("unknown calc type = " + item);
        }
        return calc.list(companyId, shopId, agentId, in, item);
    }

    @AllArgsConstructor
    public static class SafeFuture<T> {
        private Future<T> future;

        @SneakyThrows
        public T get() {
            return future.get();
        }
    }
}