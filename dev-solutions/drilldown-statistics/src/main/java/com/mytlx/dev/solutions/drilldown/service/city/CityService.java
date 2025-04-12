package com.mytlx.dev.solutions.drilldown.service.city;

import com.mytlx.dev.solutions.drilldown.entity.City;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-12 16:58
 */
@Slf4j
@Service
public class CityService {

    public List<City> getListByIds(List<Object> ids) {
        return null;
    }

    public List<City> getListByArea(String areaName) {
        return new ArrayList<>();
    }
}
