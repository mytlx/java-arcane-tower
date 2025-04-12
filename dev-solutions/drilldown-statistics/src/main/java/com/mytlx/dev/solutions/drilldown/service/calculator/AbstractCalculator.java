package com.mytlx.dev.solutions.drilldown.service.calculator;

import com.mytlx.arcane.utils.Async;
import com.mytlx.arcane.utils.Lang;
import com.mytlx.dev.solutions.drilldown.enums.BigAreaEnum;
import com.mytlx.dev.solutions.drilldown.enums.CalculatorItem;
import com.mytlx.dev.solutions.drilldown.router.In;
import com.mytlx.dev.solutions.drilldown.service.city.CityService;
import com.mytlx.dev.solutions.drilldown.service.company.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-12 14:21
 */
@Slf4j
public abstract class AbstractCalculator implements Calculator {

    @Autowired
    private CityService cityService;
    @Autowired
    private CompanyService companyService;

    @Override
    public Map<String, Long> calcBigArea(In in, List<Integer> bigAreaIds, CalculatorItem item) throws Exception {
        if (bigAreaIds == null || bigAreaIds.isEmpty()) {
            return new HashMap<>();
        }

        return Async.submit(() -> bigAreaIds.parallelStream()
                .map(k -> {
                    try {
                        long value = 0L;
                        var bigAreaList = cityService.getListByArea(BigAreaEnum.getEnumByCode(k).getName());
                        if (bigAreaList != null && !bigAreaList.isEmpty()) {
                            List<Integer> cityIds = Lang.collect(bigAreaList, "id");
                            Map<String, Long> cityValues = calcCity(in, cityIds, item);
                            value = cityValues.values().stream().mapToLong(c -> c).sum();
                        }
                        return new ImmutablePair<>(k.toString(), value);
                    } catch (Exception e) {
                        log.error("query bigAreaIds = {}", bigAreaIds, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(ImmutablePair::getLeft, ImmutablePair::getRight))
        ).get();
    }

    @Override
    public Map<String, Long> calcCity(In in, List<Integer> cityIds, CalculatorItem item) throws Exception {
        if (cityIds == null || cityIds.isEmpty()) {
            return new HashMap<>();
        }

        return Async.submit(() -> cityIds.parallelStream()
                .map(k -> {
                    try {
                        long value = 0L;
                        var companyList = companyService.getListByCityId(k);
                        if (companyList != null && !companyList.isEmpty()) {
                            List<Long> companyIds = Lang.collect(companyList, "id");
                            Map<String, Long> companyValues = calcCompany(in, companyIds, item);
                            value = companyValues.values().stream().mapToLong(c -> c).sum();
                        }
                        return new ImmutablePair<>(k.toString(), value);
                    } catch (Exception e) {
                        log.error("query cityIds = {}", cityIds, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(ImmutablePair::getLeft, ImmutablePair::getRight))
        ).join();
    }
}
