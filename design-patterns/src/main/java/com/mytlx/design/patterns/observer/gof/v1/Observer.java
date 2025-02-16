package com.mytlx.design.patterns.observer.gof.v1;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-16 21:57
 */
public interface Observer {
    void update(float temp, float humidity, float pressure);
}
