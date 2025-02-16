package com.mytlx.design.patterns.observer.gof.v2jdk;

import java.util.Observable;
import java.util.Observer;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-16 22:29
 */
@SuppressWarnings("deprecation")
public class CurrentConditionsDisplay implements Observer, DisplayElement {

    private float temperature;
    private float humidity;
    private WeatherData weatherData;


    public CurrentConditionsDisplay(WeatherData weatherData) {
        this.weatherData = weatherData;
        weatherData.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof WeatherData wea) {
            this.temperature = wea.getTemperature();
            this.humidity = wea.getHumidity();
            display();
        }
    }

    @Override
    public void display() {
        System.out.println("Current conditions: " + temperature + "F degrees and " + humidity + "% humidity");
    }
}
