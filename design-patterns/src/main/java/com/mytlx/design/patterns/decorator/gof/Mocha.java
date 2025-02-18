package com.mytlx.design.patterns.decorator.gof;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-18 21:33
 */
public class Mocha extends CondimentDecorator {

    Beverage beverage;

    public Mocha(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", Mocha";
    }

    @Override
    public double cost() {
        return .20 + beverage.cost();
    }
}
