package com.mytlx.reflection.practice._10_dynamic_proxy;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-15 18:53
 */
public class ServiceImpl implements Service {
    @Override
    public void execute() {
        System.out.println("Executing service...");
    }
}
