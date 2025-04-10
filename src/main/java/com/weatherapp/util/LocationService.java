package com.weatherapp.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

/**
 * Servicio para detectar la ubicación actual del usuario mediante su dirección IP.
 */
public class LocationService {
    
    // Coordenadas por defecto (solo se usan si falla la detección)
    private double latitude = 19.432608;
    private double longitude = -99.133209;
    private String city = "Ciudad de México";
    
    public LocationService() {
        try {
            // Intentar obtener la ubicación basada en IP con ipapi
            System.out.println("Detectando ubicación actual...");
            
            // Intentamos primero con ipinfo.io
            try {
                String jsonResponse = getLocationFromIpinfo();
                JSONObject locationData = new JSONObject(jsonResponse);
                
                if (locationData.has("city")) {
                    city = locationData.getString("city");
                }
                
                if (locationData.has("latitude") && locationData.has("longitude")) {
                    latitude = locationData.getDouble("latitude");
                    longitude = locationData.getDouble("longitude");
                    System.out.println("Ubicación detectada (ipinfo): " + city + " (" + latitude + ", " + longitude + ")");
                    return;
                }
            } catch (Exception e) {
                System.err.println("Error con ipinfo.io: " + e.getMessage());
                // Continuamos con el siguiente proveedor
            }
            
            // Si ipinfo falla, intentamos con ipapi.co
            try {
                String jsonResponse = getLocationFromIpapi();
                JSONObject locationData = new JSONObject(jsonResponse);
                
                if (locationData.has("city")) {
                    city = locationData.getString("city");
                }
                
                if (locationData.has("latitude") && locationData.has("longitude")) {
                    latitude = locationData.getDouble("latitude");
                    longitude = locationData.getDouble("longitude");
                    System.out.println("Ubicación detectada (ipapi): " + city + " (" + latitude + ", " + longitude + ")");
                    return;
                }
            } catch (Exception e) {
                System.err.println("Error con ipapi.co: " + e.getMessage());
                // Continuamos con el siguiente método
            }
            
            // Si todo falla, intentamos con ip-api.com
            try {
                String jsonResponse = getLocationFromIpApiCom();
                JSONObject locationData = new JSONObject(jsonResponse);
                
                if (locationData.has("city")) {
                    city = locationData.getString("city");
                }
                
                if (locationData.has("lat") && locationData.has("lon")) {
                    latitude = locationData.getDouble("lat");
                    longitude = locationData.getDouble("lon");
                    System.out.println("Ubicación detectada (ip-api): " + city + " (" + latitude + ", " + longitude + ")");
                    return;
                }
            } catch (Exception e) {
                System.err.println("Error con ip-api.com: " + e.getMessage());
            }
            
            // Si llegamos aquí, ningún servicio funcionó
            System.err.println("No se pudo determinar automáticamente la ubicación.");
            System.out.println("Usando ubicación por defecto: " + city);
            
        } catch (Exception e) {
            System.err.println("Error al obtener la ubicación: " + e.getMessage());
            e.printStackTrace();
            System.out.println("Usando ubicación por defecto: " + city);
        }
    }
    
    /**
     * Obtiene los datos de geolocalización desde ipinfo.io
     */
    private String getLocationFromIpinfo() throws Exception {
        URL url = new URL("https://ipinfo.io/json");
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
        
        // Procesar la respuesta para extraer datos necesarios
        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONObject result = new JSONObject();
        
        if (jsonResponse.has("city")) {
            result.put("city", jsonResponse.getString("city"));
        }
        
        if (jsonResponse.has("loc")) {
            String[] coords = jsonResponse.getString("loc").split(",");
            if (coords.length == 2) {
                result.put("latitude", Double.parseDouble(coords[0]));
                result.put("longitude", Double.parseDouble(coords[1]));
            }
        }
        
        return result.toString();
    }
    
    /**
     * Obtiene los datos de geolocalización desde ipapi.co
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
     * Obtiene los datos de geolocalización desde ip-api.com
     */
    private String getLocationFromIpApiCom() throws Exception {
        URL url = new URL("http://ip-api.com/json");
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
     * Obtiene la latitud actual.
     */
    public double getLatitude() {
        return latitude;
    }
    
    /**
     * Obtiene la longitud actual.
     */
    public double getLongitude() {
        return longitude;
    }
    
    /**
     * Obtiene el nombre de la ciudad actual.
     */
    public String getCurrentCity() {
        return city;
    }
}