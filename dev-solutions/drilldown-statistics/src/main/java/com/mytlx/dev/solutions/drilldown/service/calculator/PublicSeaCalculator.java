package com.mytlx.dev.solutions.drilldown.service.calculator;

import com.mytlx.dev.solutions.drilldown.enums.CalculatorItem;
import com.mytlx.dev.solutions.drilldown.router.CrmPage;
import com.mytlx.dev.solutions.drilldown.router.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-12 14:51
 */
@Slf4j
@Component
public class PublicSeaCalculator extends AbstractCalculator implements Calculator {
    @Override
    public Map<String, Long> calcCompany(In in, List<Long> companyIds, CalculatorItem item) throws Exception {
        return Map.of();
    }

    @Override
    public Map<String, Long> calcAgent(In in, Long companyId, Long shopId, List<Long> agentIds, CalculatorItem item) throws Exception {
        return Map.of();
    }

    @Override
    public CrmPage<?> list(Long companyId, Long shopId, Long agentId, In in, CalculatorItem clickItem) {
        return null;
    }
}
