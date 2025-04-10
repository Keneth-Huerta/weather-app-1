package com.weatherapp.model;

import java.util.List;

public class Forecast {
    private String cityName;
    private List<DailyForecast> dailyForecasts;

    public Forecast(String cityName, List<DailyForecast> dailyForecasts) {
        this.cityName = cityName;
        this.dailyForecasts = dailyForecasts;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<DailyForecast> getDailyForecasts() {
        return dailyForecasts;
    }

    public void setDailyForecasts(List<DailyForecast> dailyForecasts) {
        this.dailyForecasts = dailyForecasts;
    }

    public static class DailyForecast {
        private String date;
        private double temperature;
        private String weatherDescription;

        public DailyForecast(String date, double temperature, String weatherDescription) {
            this.date = date;
            this.temperature = temperature;
            this.weatherDescription = weatherDescription;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public double getTemperature() {
            return temperature;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        public String getWeatherDescription() {
            return weatherDescription;
        }

        public void setWeatherDescription(String weatherDescription) {
            this.weatherDescription = weatherDescription;
        }
    }
}