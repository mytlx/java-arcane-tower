package com.mytlx.dev.solutions.drilldown.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-12 16:56
 */
@Data
@Accessors(chain = true)
public class City {

    private Long id;
    private String name;
}
