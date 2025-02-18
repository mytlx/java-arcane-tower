package com.mytlx.design.patterns.decorator.gof;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-18 21:32
 */
public class HouseBlend implements Beverage {

    @Override
    public double cost() {
        return .89;
    }

    @Override
    public String getDescription() {
        return "House Blend Coffee";
    }
}
