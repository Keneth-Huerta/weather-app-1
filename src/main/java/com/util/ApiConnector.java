package com.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Clase que gestiona las conexiones con la API de OpenWeatherMap.
 * Proporciona métodos para obtener datos del clima actual y pronósticos.
 * 
 * @author Weather App Team
 * @version 1.0
 */
public class ApiConnector {
    private final String apiKey;
    private final String baseUrl;

    /**
     * Constructor que inicializa el conector con la clave API proporcionada.
     * 
     * @param apiKey La clave API de OpenWeatherMap
     */
    public ApiConnector(String apiKey) {
        this.apiKey = apiKey;
        this.baseUrl = "https://api.openweathermap.org/data/2.5/";
    }

    /**
     * Obtiene datos del clima actual para una ciudad específica.
     * 
     * @param city Nombre de la ciudad
     * @return Datos del clima en formato JSON como cadena
     * @throws Exception Si ocurre un error en la petición o la API key es inválida
     */
    public String getCurrentWeatherByCity(String city) throws Exception {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your_openweathermap_api_key_here")) {
            throw new IllegalArgumentException("Clave API inválida. Por favor, configura una clave API válida en config.properties");
        }
        String endpoint = String.format("weather?q=%s&appid=%s&units=metric&lang=es", city, apiKey);
        return sendRequest(endpoint);
    }
    
    /**
     * Obtiene datos del clima actual para unas coordenadas específicas.
     * 
     * @param lat Latitud de la ubicación
     * @param lon Longitud de la ubicación
     * @return Datos del clima en formato JSON como cadena
     * @throws Exception Si ocurre un error en la petición o la API key es inválida
     */
    public String getCurrentWeatherByCoords(double lat, double lon) throws Exception {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your_openweathermap_api_key_here")) {
            throw new IllegalArgumentException("Clave API inválida. Por favor, configura una clave API válida en config.properties");
        }
        String endpoint = String.format("weather?lat=%.6f&lon=%.6f&appid=%s&units=metric&lang=es", lat, lon, apiKey);
        return sendRequest(endpoint);
    }

    /**
     * Obtiene datos de pronóstico para una ciudad específica.
     * 
     * @param city Nombre de la ciudad
     * @return Datos del pronóstico en formato JSON como cadena
     * @throws Exception Si ocurre un error en la petición o la API key es inválida
     */
    public String getForecastByCity(String city) throws Exception {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your_openweathermap_api_key_here")) {
            throw new IllegalArgumentException("Clave API inválida. Por favor, configura una clave API válida en config.properties");
        }
        String endpoint = String.format("forecast?q=%s&appid=%s&units=metric&lang=es", city, apiKey);
        return sendRequest(endpoint);
    }
    
    /**
     * Obtiene datos de pronóstico para unas coordenadas específicas.
     * 
     * @param lat Latitud de la ubicación
     * @param lon Longitud de la ubicación
     * @return Datos del pronóstico en formato JSON como cadena
     * @throws Exception Si ocurre un error en la petición o la API key es inválida
     */
    public String getForecastByCoords(double lat, double lon) throws Exception {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your_openweathermap_api_key_here")) {
            throw new IllegalArgumentException("Clave API inválida. Por favor, configura una clave API válida en config.properties");
        }
        String endpoint = String.format("forecast?lat=%.6f&lon=%.6f&appid=%s&units=metric&lang=es", lat, lon, apiKey);
        return sendRequest(endpoint);
    }

    /**
     * Método privado que realiza la petición HTTP a la API.
     * 
     * @param endpoint La ruta del endpoint con parámetros
     * @return La respuesta de la API como cadena
     * @throws Exception Si ocurre un error en la conexión o respuesta
     */
    private String sendRequest(String endpoint) throws Exception {
        URL url = new URL(baseUrl + endpoint);
        System.out.println("Solicitando: " + url);
        
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            if (responseCode == 401) {
                throw new RuntimeException("Error HTTP 401: No autorizado. Por favor, verifica tu clave API.");
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