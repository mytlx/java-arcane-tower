package com.mytlx.design.patterns.observer.gof.v3;

/**
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-16 21:57
 */
public interface Subject {

    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
    void notifyObservers(Object arg);
}
