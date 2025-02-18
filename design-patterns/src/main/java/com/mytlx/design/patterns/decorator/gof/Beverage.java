package com.mytlx.design.patterns.decorator.gof;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-18 21:29
 */
public interface Beverage {
    double cost();

    default String getDescription() {
        return "Unknown Beverage";
    }
}
