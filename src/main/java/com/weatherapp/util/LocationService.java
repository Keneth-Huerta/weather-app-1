package com.weatherapp.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

/**
 * Servicio que detecta la ubicación actual del usuario.
 * Utiliza servicios de geolocalización basados en IP para determinar
 * las coordenadas y la ciudad del usuario.
 * 
 * @author Weather App Team
 * @version 1.0
 */
public class LocationService {
    
    // Coordenadas predeterminadas (CDMX)
    private double latitude = 19.4326;
    private double longitude = -99.1332;
    private String city = "Ciudad de México";
    
    /**
     * Constructor que intenta detectar la ubicación del usuario.
     * Si la detección falla, se usa una ubicación predeterminada.
     */
    public LocationService() {
        try {
            // Intentar obtener la ubicación basada en IP con diversos servicios
            System.out.println("Detectando ubicación actual...");
            
            // Intentamos con ip-api.com (suele tener mejor detección)
            try {
                String jsonResponse = getLocationFromIpApiCom();
                JSONObject locationData = new JSONObject(jsonResponse);
                
                if (locationData.has("status") && "success".equals(locationData.getString("status"))) {
                    // Traducir el nombre de la ciudad al español si es necesario
                    String cityName = locationData.getString("city");
                    city = translateCityName(cityName);
                    
                    latitude = locationData.getDouble("lat");
                    longitude = locationData.getDouble("lon");
                    System.out.println("Ubicación detectada (ip-api): " + city + " (" + latitude + ", " + longitude + ")");
                    return;
                }
            } catch (Exception e) {
                System.err.println("Error con ip-api.com: " + e.getMessage());
            }
            
            // Si ip-api falla, intentamos con ipapi.co
            try {
                String jsonResponse = getLocationFromIpapi();
                JSONObject locationData = new JSONObject(jsonResponse);
                
                if (locationData.has("city")) {
                    String cityName = locationData.getString("city");
                    city = translateCityName(cityName);
                    
                    latitude = locationData.getDouble("latitude");
                    longitude = locationData.getDouble("longitude");
                    System.out.println("Ubicación detectada (ipapi): " + city + " (" + latitude + ", " + longitude + ")");
                    return;
                }
            } catch (Exception e) {
                System.err.println("Error con ipapi.co: " + e.getMessage());
            }
            
            // Si llegamos aquí, ningún servicio funcionó correctamente
            System.out.println("Usando ubicación predeterminada: " + city);
            
        } catch (Exception e) {
            System.err.println("Error al obtener la ubicación: " + e.getMessage());
            System.out.println("Usando ubicación predeterminada: " + city);
        }
    }
    
    /**
     * Traduce los nombres comunes de ciudades al español.
     * 
     * @param cityName Nombre de la ciudad en el idioma original
     * @return Nombre de la ciudad traducido al español
     */
    private String translateCityName(String cityName) {
        if (cityName == null) return "Ciudad de México";
        
        switch (cityName) {
            case "Mexico City": return "Ciudad de México";
            case "Guadalajara": return "Guadalajara";
            case "Monterrey": return "Monterrey";
            case "Tijuana": return "Tijuana";
            case "Puebla": return "Puebla";
            case "Juarez": case "Ciudad Juarez": return "Ciudad Juárez";
            case "Leon": return "León";
            case "Zapopan": return "Zapopan";
            case "Nezahualcoyotl": return "Nezahualcóyotl";
            case "Chihuahua": return "Chihuahua";
            case "Naucalpan": return "Naucalpan";
            case "Merida": return "Mérida";
            case "San Luis Potosi": return "San Luis Potosí";
            case "Aguascalientes": return "Aguascalientes";
            case "Hermosillo": return "Hermosillo";
            case "Saltillo": return "Saltillo";
            case "Mexicali": return "Mexicali";
            case "Culiacan": return "Culiacán";
            case "Queretaro": return "Querétaro";
            case "Morelia": return "Morelia";
            case "Cancun": return "Cancún";
            case "Torreon": return "Torreón";
            case "Acapulco": return "Acapulco";
            case "Villahermosa": return "Villahermosa";
            case "Veracruz": return "Veracruz";
            case "Tuxtla Gutierrez": return "Tuxtla Gutiérrez";
            case "New York": return "Nueva York";
            case "London": return "Londres";
            case "Paris": return "París";
            case "Rome": return "Roma";
            case "Madrid": return "Madrid";
            case "Berlin": return "Berlín";
            case "Tokyo": return "Tokio";
            case "Beijing": return "Pekín";
            case "Moscow": return "Moscú";
            default: return cityName; // Si no está en la lista, devolver el nombre original
        }
    }
    
    /**
     * Obtiene datos de geolocalización desde ipapi.co.
     * 
     * @return Respuesta JSON como cadena
     * @throws Exception Si ocurre un error en la conexión o respuesta
     */
    private String getLocationFromIpapi() throws Exception {
        URL url = new URL("https://ipapi.co/json/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Error HTTP: " + responseCode);
        }
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        
        return response.toString();
    }
    
    /**
     * Obtiene datos de geolocalización desde ip-api.com.
     * 
     * @return Respuesta JSON como cadena
     * @throws Exception Si ocurre un error en la conexión o respuesta
     */
    private String getLocationFromIpApiCom() throws Exception {
        URL url = new URL("http://ip-api.com/json?lang=es");  // Solicitar respuesta en español
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Error HTTP: " + responseCode);
        }
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        
        return response.toString();
    }

    /**
     * Obtiene la latitud de la ubicación actual.
     * 
     * @return Latitud en grados decimales
     */
    public double getLatitude() {
        return latitude;
    }
    
    /**
     * Obtiene la longitud de la ubicación actual.
     * 
     * @return Longitud en grados decimales
     */
    public double getLongitude() {
        return longitude;
    }
    
    /**
     * Obtiene el nombre de la ciudad actual.
     * 
     * @return Nombre de la ciudad
     */
    public String getCurrentCity() {
        return city;
    }
}