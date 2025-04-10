package com.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiConnector {
    private final String apiKey;
    private final String baseUrl;

    public ApiConnector(String apiKey) {
        this.apiKey = apiKey;
        this.baseUrl = "https://api.openweathermap.org/data/2.5/";
    }

    public String getCurrentWeatherByCity(String city) throws Exception {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your_openweathermap_api_key_here")) {
            throw new IllegalArgumentException("Invalid API key. Please set a valid API key in config.properties");
        }
        String endpoint = String.format("weather?q=%s&appid=%s&units=metric&lang=es", city, apiKey);
        return sendRequest(endpoint);
    }
    
    public String getCurrentWeatherByCoords(double lat, double lon) throws Exception {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your_openweathermap_api_key_here")) {
            throw new IllegalArgumentException("Invalid API key. Please set a valid API key in config.properties");
        }
        String endpoint = String.format("weather?lat=%.6f&lon=%.6f&appid=%s&units=metric&lang=es", lat, lon, apiKey);
        return sendRequest(endpoint);
    }

    public String getForecastByCity(String city) throws Exception {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your_openweathermap_api_key_here")) {
            throw new IllegalArgumentException("Invalid API key. Please set a valid API key in config.properties");
        }
        String endpoint = String.format("forecast?q=%s&appid=%s&units=metric&lang=es", city, apiKey);
        return sendRequest(endpoint);
    }
    
    public String getForecastByCoords(double lat, double lon) throws Exception {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your_openweathermap_api_key_here")) {
            throw new IllegalArgumentException("Invalid API key. Please set a valid API key in config.properties");
        }
        String endpoint = String.format("forecast?lat=%.6f&lon=%.6f&appid=%s&units=metric&lang=es", lat, lon, apiKey);
        return sendRequest(endpoint);
    }

    private String sendRequest(String endpoint) throws Exception {
        URL url = new URL(baseUrl + endpoint);
        System.out.println("Requesting: " + url);
        
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            if (responseCode == 401) {
                throw new RuntimeException("Error HTTP 401: Unauthorized. Por favor, verifica tu API key.");
            } else if (responseCode == 404) {
                throw new RuntimeException("Error HTTP 404: Ciudad o coordenadas no encontradas.");
            } else {
                throw new RuntimeException("Error HTTP: " + responseCode);
            }
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
        StringBuilder response = new StringBuilder();
        String output;

        while ((output = br.readLine()) != null) {
            response.append(output);
        }

        connection.disconnect();
        return response.toString();
    }
}