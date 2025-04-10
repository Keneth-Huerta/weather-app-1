package com.weatherapp.model;

/**
 * Modelo que representa los datos del clima actual para una ubicación.
 * 
 * @author Weather App Team
 * @version 1.0
 */
public class CurrentWeather {
    private String cityName;
    private String description;
    private double temperature;
    private double humidity;
    private double windSpeed;

    /**
     * Constructor que inicializa todos los campos del clima actual.
     * 
     * @param cityName Nombre de la ciudad
     * @param description Descripción del clima (ej. "cielo despejado")
     * @param temperature Temperatura en grados Celsius
     * @param humidity Humedad relativa en porcentaje
     * @param windSpeed Velocidad del viento en m/s
     */
    public CurrentWeather(String cityName, String description, double temperature, double humidity, double windSpeed) {
        this.cityName = cityName;
        this.description = description;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
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
     * Obtiene la descripción del clima.
     * 
     * @return Descripción textual del clima
     */
    public String getDescription() {
        return description;
    }

    /**
     * Obtiene la temperatura actual.
     * 
     * @return Temperatura en grados Celsius
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Obtiene el porcentaje de humedad.
     * 
     * @return Humedad relativa en porcentaje
     */
    public double getHumidity() {
        return humidity;
    }

    /**
     * Obtiene la velocidad del viento.
     * 
     * @return Velocidad del viento en metros por segundo
     */
    public double getWindSpeed() {
        return windSpeed;
    }

    /**
     * Representación en texto del objeto.
     * 
     * @return Texto que representa el objeto y sus propiedades
     */
    @Override
    public String toString() {
        return "CurrentWeather{" +
                "cityName='" + cityName + '\'' +
                ", description='" + description + '\'' +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", windSpeed=" + windSpeed +
                '}';
    }
}