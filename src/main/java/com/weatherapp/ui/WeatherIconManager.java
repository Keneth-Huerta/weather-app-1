package com.weatherapp.ui;

import javax.swing.ImageIcon;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase encargada de administrar los iconos del clima según la condición meteorológica.
 * Usa directamente los iconos proporcionados por OpenWeatherMap.
 * 
 * @author Weather App Team
 * @version 1.0
 */
public class WeatherIconManager {
    
    private static final String ICON_URL_BASE = "https://openweathermap.org/img/wn/";
    private static final String ICON_URL_SUFFIX = "@2x.png"; // Para versión grande (100x100px)
    private static final Map<String, ImageIcon> iconCache = new HashMap<>();
    
    /**
     * Obtiene un ImageIcon basado en el código del icono proporcionado por la API.
     * 
     * @param iconCode Código del icono proporcionado por OpenWeatherMap (ej. "01d", "10n")
     * @return Un objeto ImageIcon correspondiente a la condición climática
     */
    public static ImageIcon getIconByCode(String iconCode) {
        // Si no hay código, usar un icono predeterminado
        if (iconCode == null || iconCode.isEmpty()) {
            iconCode = "01d"; // Cielo despejado (día)
        }
        
        // Verificar si el icono ya está en caché
        if (iconCache.containsKey(iconCode)) {
            return iconCache.get(iconCode);
        }
        
        // Si no está en caché, intentar descargarlo
        try {
            URL iconUrl = new URL(ICON_URL_BASE + iconCode + ICON_URL_SUFFIX);
            ImageIcon icon = new ImageIcon(iconUrl);
            
            // Guardar en caché para uso futuro
            iconCache.put(iconCode, icon);
            
            return icon;
        } catch (Exception e) {
            System.err.println("Error al cargar el icono " + iconCode + ": " + e.getMessage());
            
            // Si hay error al descargar, intentar con un icono predeterminado
            if (!iconCode.equals("01d")) {
                return getIconByCode("01d");
            }
            
            return null;
        }
    }
    
    /**
     * Obtiene un ImageIcon basado en la descripción del clima.
     * Este método es para mantener compatibilidad con código anterior.
     * 
     * @param weatherDescription Descripción del clima proporcionada por la API
     * @return Un objeto ImageIcon correspondiente a la condición climática
     */
    public static ImageIcon getIconForWeather(String weatherDescription) {
        // Mapeo de descripciones comunes a códigos de iconos
        // Esto es solo para compatibilidad con código antiguo
        String iconCode = "01d"; // Predeterminado: cielo despejado
        
        if (weatherDescription == null || weatherDescription.isEmpty()) {
            return getIconByCode(iconCode);
        }
        
        String desc = weatherDescription.toLowerCase();
        
        if (desc.contains("despejado") || desc.contains("clear")) {
            iconCode = "01d";
        } else if (desc.contains("algo de nubes") || desc.contains("pocas nubes") || desc.contains("few clouds")) {
            iconCode = "02d";
        } else if (desc.contains("nubes dispersas") || desc.contains("scattered clouds")) {
            iconCode = "03d";
        } else if (desc.contains("muy nuboso") || desc.contains("nubes rotas") || desc.contains("broken clouds")) {
            iconCode = "04d";
        } else if (desc.contains("lluvia ligera") || desc.contains("light rain")) {
            iconCode = "10d";
        } else if (desc.contains("lluvia") || desc.contains("rain")) {
            iconCode = "09d";
        } else if (desc.contains("tormenta") || desc.contains("thunderstorm")) {
            iconCode = "11d";
        } else if (desc.contains("nieve") || desc.contains("snow")) {
            iconCode = "13d";
        } else if (desc.contains("niebla") || desc.contains("bruma") || desc.contains("mist") || desc.contains("fog")) {
            iconCode = "50d";
        }
        
        return getIconByCode(iconCode);
    }
}