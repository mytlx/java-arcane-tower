package com.mytlx.dev.solutions.drilldown.service.company;

import com.mytlx.dev.solutions.drilldown.entity.Company;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-12 17:01
 */
@Slf4j
@Service
public class CompanyService {

    public List<Company> getListByIds(List<Object> rangeIds) {
        return null;
    }

    public List<Company> getListByCityId(Integer cityId) {
        return new ArrayList<>();
    }
}
