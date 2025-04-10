package com.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Gestor de configuración que carga y proporciona acceso a propiedades
 * almacenadas en el archivo config.properties.
 * 
 * Implementa el patrón Singleton para garantizar una única instancia.
 * 
 * @author
 * @version 1.0
 */
public class ConfigManager {
    private static Properties properties;
    private static final String propertiesFilePath = "/config.properties";
    private static ConfigManager instance;
    private static boolean configLoaded = false;
    // Clave API predeterminada
    private static final String DEFAULT_API_KEY = "00a0d0cf1a2317512849d803c85f5e86";

    /**
     * Constructor privado que carga las propiedades.
     * Parte del patrón Singleton.
     */
    private ConfigManager() {
        properties = new Properties();
        loadProperties();
    }

    /**
     * Obtiene la instancia única del gestor de configuración.
     * 
     * @return La instancia de ConfigManager
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    /**
     * Carga las propiedades desde el archivo de configuración.
     * Si no se puede encontrar el archivo, usa valores predeterminados.
     */
    private void loadProperties() {
        try (InputStream input = getClass().getResourceAsStream(propertiesFilePath)) {
            if (input == null) {
                System.err.println("ERROR: Unable to find " + propertiesFilePath);
                System.err.println("Please create this file in src/main/resources directory");
                // Use default properties as fallback
                setDefaultProperties();
                return;
            }
            properties.load(input);
            
            // Validate API key
            String apiKey = properties.getProperty("api.key");
            if (apiKey == null || apiKey.isEmpty() || "your_openweathermap_api_key_here".equals(apiKey) || 
                "YOUR_API_KEY_HERE".equals(apiKey)) {
                // Use the provided API key
                properties.setProperty("api.key", DEFAULT_API_KEY);
            }
            
            configLoaded = true;
        } catch (IOException ex) {
            System.err.println("ERROR: Failed to load properties file: " + ex.getMessage());
            // Use default properties as fallback
            setDefaultProperties();
            ex.printStackTrace();
        }
    }

    /**
     * Establece valores predeterminados para las propiedades.
     * Se usa cuando no se puede cargar el archivo de configuración.
     */
    private void setDefaultProperties() {
        // Use the provided API key as default
        properties.setProperty("api.key", DEFAULT_API_KEY);
        properties.setProperty("default.city", "Madrid");
        properties.setProperty("default.latitude", "40.4165");
        properties.setProperty("default.longitude", "-3.7026");
        properties.setProperty("refresh.interval", "1800000");
    }

    /**
     * Obtiene el valor de una propiedad específica.
     * 
     * @param key Clave de la propiedad
     * @return Valor de la propiedad o null si no existe
     */
    public static String getProperty(String key) {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return properties.getProperty(key);
    }

    /**
     * Obtiene la clave API de OpenWeatherMap.
     * 
     * @return La clave API configurada
     */
    public static String getApiKey() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return properties.getProperty("api.key");
    }
    
    /**
     * Verifica si la configuración se cargó correctamente.
     * 
     * @return true si la configuración se cargó correctamente, false en caso contrario
     */
    public static boolean isConfigLoaded() {
        return configLoaded;
    }
}