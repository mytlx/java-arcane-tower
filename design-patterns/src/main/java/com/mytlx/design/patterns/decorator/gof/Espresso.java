package com.mytlx.design.patterns.decorator.gof;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-18 21:31
 */
public class Espresso implements Beverage {

    @Override
    public double cost() {
        return 1.99;
    }

    @Override
    public String getDescription() {
        return "Espresso";
    }
}
