package com.weatherapp.model;

import java.util.List;

/**
 * Modelo que representa el pronóstico del clima para los próximos días/horas.
 * 
 * @author Weather App Team
 * @version 1.0
 */
public class Forecast {
    private String cityName;
    private List<DailyForecast> dailyForecasts;

    /**
     * Constructor que inicializa el pronóstico.
     * 
     * @param cityName Nombre de la ciudad
     * @param dailyForecasts Lista de pronósticos para cada día/hora
     */
    public Forecast(String cityName, List<DailyForecast> dailyForecasts) {
        this.cityName = cityName;
        this.dailyForecasts = dailyForecasts;
    }

    /**
     * Obtiene el nombre de la ciudad.
     * 
     * @return Nombre de la ciudad
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Establece el nombre de la ciudad.
     * 
     * @param cityName Nuevo nombre de la ciudad
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * Obtiene la lista de pronósticos diarios/horarios.
     * 
     * @return Lista de pronósticos
     */
    public List<DailyForecast> getDailyForecasts() {
        return dailyForecasts;
    }

    /**
     * Establece la lista de pronósticos diarios/horarios.
     * 
     * @param dailyForecasts Nueva lista de pronósticos
     */
    public void setDailyForecasts(List<DailyForecast> dailyForecasts) {
        this.dailyForecasts = dailyForecasts;
    }

    /**
     * Clase interna que representa un pronóstico para un día u hora específica.
     */
    public static class DailyForecast {
        private String date;
        private double temperature;
        private String weatherDescription;

        /**
         * Constructor para un pronóstico individual.
         * 
         * @param date Fecha y hora del pronóstico
         * @param temperature Temperatura prevista en grados Celsius
         * @param weatherDescription Descripción textual del clima
         */
        public DailyForecast(String date, double temperature, String weatherDescription) {
            this.date = date;
            this.temperature = temperature;
            this.weatherDescription = weatherDescription;
        }

        /**
         * Obtiene la fecha del pronóstico.
         * 
         * @return Fecha formateada
         */
        public String getDate() {
            return date;
        }

        /**
         * Establece la fecha del pronóstico.
         * 
         * @param date Nueva fecha formateada
         */
        public void setDate(String date) {
            this.date = date;
        }

        /**
         * Obtiene la temperatura del pronóstico.
         * 
         * @return Temperatura en grados Celsius
         */
        public double getTemperature() {
            return temperature;
        }

        /**
         * Establece la temperatura del pronóstico.
         * 
         * @param temperature Nueva temperatura en grados Celsius
         */
        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        /**
         * Obtiene la descripción del clima.
         * 
         * @return Descripción textual del clima
         */
        public String getWeatherDescription() {
            return weatherDescription;
        }

        /**
         * Establece la descripción del clima.
         * 
         * @param weatherDescription Nueva descripción textual
         */
        public void setWeatherDescription(String weatherDescription) {
            this.weatherDescription = weatherDescription;
        }
    }
}