package com.mytlx.dev.solutions.drilldown.service.calculator;

import com.mytlx.dev.solutions.drilldown.enums.CalculatorItem;
import com.mytlx.dev.solutions.drilldown.router.CrmPage;
import com.mytlx.dev.solutions.drilldown.router.In;

import java.util.List;
import java.util.Map;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-12 14:13
 */
public interface Calculator {
    Map<String, Long> calcBigArea(In in, List<Integer> bigAreaIds, CalculatorItem item) throws Exception;

    Map<String, Long> calcCity(In in, List<Integer> cityIds, CalculatorItem item) throws Exception;

    Map<String, Long> calcCompany(In in, List<Long> companyIds, CalculatorItem item) throws Exception;

    Map<String, Long> calcAgent(In in, Long companyId, Long shopId, List<Long> agentIds, CalculatorItem item) throws Exception;

    CrmPage<?> list(Long companyId, Long shopId, Long agentId, In in, CalculatorItem clickItem);

}
