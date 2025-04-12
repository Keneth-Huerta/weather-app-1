package com.weatherapp.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
    private String locality = ""; // Para almacenar la ubicación más específica (colonia/barrio)
    
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
                    // Guardamos ciudad y distrito/colonia si está disponible
                    String cityName = locationData.getString("city");
                    city = translateCityName(cityName);
                    
                    // Si hay información del distrito o colonia, guardarla
                    if (locationData.has("district") && !locationData.getString("district").isEmpty()) {
                        locality = locationData.getString("district");
                    }
                    
                    latitude = locationData.getDouble("lat");
                    longitude = locationData.getDouble("lon");
                    
                    // Imprimir información detallada de ubicación para prueba
                    printLocationInfo(city, locality, latitude, longitude, "ip-api.com");
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
                    
                    // Imprimir información detallada de ubicación para prueba
                    printLocationInfo(city, locality, latitude, longitude, "ipapi.co");
                    return;
                }
            } catch (Exception e) {
                System.err.println("Error con ipapi.co: " + e.getMessage());
            }
            
            // Si llegamos aquí, ningún servicio funcionó correctamente
            System.out.println("Usando ubicación predeterminada: " + city);
            // Imprimir coordenadas predeterminadas
            printLocationInfo(city, locality, latitude, longitude, "predeterminada");
            
        } catch (Exception e) {
            System.err.println("Error al obtener la ubicación: " + e.getMessage());
            System.out.println("Usando ubicación predeterminada: " + city);
            // Imprimir coordenadas predeterminadas en caso de error
            printLocationInfo(city, locality, latitude, longitude, "predeterminada (error)");
        }
    }
    
    /**
     * Imprime información detallada de la ubicación detectada para pruebas.
     * 
     * @param city Nombre de la ciudad
     * @param locality Nombre de la colonia o barrio (puede estar vacío)
     * @param lat Latitud detectada
     * @param lon Longitud detectada
     * @param source Fuente de la información de ubicación
     */
    private void printLocationInfo(String city, String locality, double lat, double lon, String source) {
        System.out.println("==============================================");
        System.out.println("INFORMACIÓN DETALLADA DE UBICACIÓN");
        System.out.println("==============================================");
        
        if (locality != null && !locality.isEmpty()) {
            System.out.println("Ubicación específica: " + locality);
            System.out.println("Ciudad: " + city);
        } else {
            System.out.println("Ciudad: " + city);
        }
        
        System.out.println("Latitud: " + lat);
        System.out.println("Longitud: " + lon);
        System.out.println("Fuente de datos: " + source);
        System.out.println("URL de Google Maps: https://www.google.com/maps?q=" + lat + "," + lon);
        System.out.println("==============================================");
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
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
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
        URL url = new URL("http://ip-api.com/json?lang=es&fields=status,message,country,city,district,lat,lon");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Error HTTP: " + responseCode);
        }
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
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
     * Si está disponible, incluye la información de localidad/barrio.
     * 
     * @return Nombre de la ubicación completa
     */
    public String getCurrentCity() {
        if (locality != null && !locality.isEmpty()) {
            // Debug: imprimir exactamente qué ubicación estamos devolviendo
            String fullLocation = locality + ", " + city;
            System.out.println("Devolviendo ubicación específica: " + fullLocation);
            return fullLocation;
        } else {
            System.out.println("Devolviendo solo ciudad: " + city);
            return city;
        }
    }
    
    /**
     * Obtiene el nombre de la localidad/colonia actual.
     * 
     * @return Nombre de la localidad/colonia
     */
    public String getLocality() {
        return locality;
    }
}