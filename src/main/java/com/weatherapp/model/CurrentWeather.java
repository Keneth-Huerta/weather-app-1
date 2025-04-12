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
    private double latitude;
    private double longitude;
    private String iconCode; // Código del icono proporcionado por OpenWeatherMap (ej. "01d", "10n")

    /**
     * Constructor que inicializa todos los campos del clima actual.
     * 
     * @param cityName Nombre de la ciudad
     * @param description Descripción del clima (ej. "cielo despejado")
     * @param temperature Temperatura en grados Celsius
     * @param humidity Humedad relativa en porcentaje
     * @param windSpeed Velocidad del viento en m/s
     * @param latitude Latitud de la ubicación
     * @param longitude Longitud de la ubicación
     * @param iconCode Código del icono según OpenWeatherMap
     */
    public CurrentWeather(String cityName, String description, double temperature, double humidity, double windSpeed, double latitude, double longitude, String iconCode) {
        this.cityName = cityName;
        this.description = description;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.latitude = latitude;
        this.longitude = longitude;
        this.iconCode = iconCode;
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
     * Establece el nombre de la ubicación (útil para mostrar información más precisa).
     * 
     * @param locationName El nombre de la ubicación específica
     */
    public void setLocationName(String locationName) {
        this.cityName = locationName;
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
     * Obtiene la latitud de la ubicación.
     * 
     * @return Latitud en grados decimales
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Obtiene la longitud de la ubicación.
     * 
     * @return Longitud en grados decimales
     */
    public double getLongitude() {
        return longitude;
    }
    
    /**
     * Obtiene el código del icono climático.
     * 
     * @return Código del icono (ej. "01d", "10n")
     */
    public String getIconCode() {
        return iconCode;
    }
    
    /**
     * Establece el código del icono climático.
     * 
     * @param iconCode Nuevo código de icono
     */
    public void setIconCode(String iconCode) {
        this.iconCode = iconCode;
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
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", iconCode='" + iconCode + '\'' +
                '}';
    }
}