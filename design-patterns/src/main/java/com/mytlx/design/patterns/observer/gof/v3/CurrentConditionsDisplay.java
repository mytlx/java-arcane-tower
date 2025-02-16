package com.mytlx.design.patterns.observer.gof.v3;


/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-16 22:05
 */
public class CurrentConditionsDisplay implements Observer, DisplayElement {

    private float temperature;
    private float humidity;

    public CurrentConditionsDisplay(WeatherData weatherData) {
        weatherData.registerObserver(this);
    }

    @Override
    public void update(Subject o, Object arg) {
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
