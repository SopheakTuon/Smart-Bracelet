package com.example.android.bluetoothlegatt.ble_service;

/**
 * @author Sopheak Tuon
 * @created on 04-Oct-17
 */

public class WeatherEntity {
    private String date;
    private String temp;
    private int weather;

    public WeatherEntity(String date, int weather, String temp) {
        this.date = date;
        this.weather = weather;
        this.temp = temp;
    }

    public String getDate() {
        return this.date;
    }

    public int getWeather() {
        return this.weather;
    }

    public String getTemp() {
        return this.temp;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setWeather(int weather) {
        this.weather = weather;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
