package com.weatherapp.controller;

import com.weatherapp.model.CurrentWeather;
import com.weatherapp.model.Forecast;
import com.weatherapp.service.WeatherService;
import com.weatherapp.ui.WeatherFrame;
import com.weatherapp.util.LocationService;
import com.util.ConfigManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * Controlador que gestiona la lógica entre la interfaz de usuario y los servicios.
 * Se encarga de obtener los datos del clima y actualizar la interfaz.
 * 
 * @author Weather App Team
 * @version 1.0
 */
public class WeatherController {

    private WeatherService weatherService;
    private WeatherFrame weatherFrame;
    private LocationService locationService;
    private Timer autoRefreshTimer;
    private final int REFRESH_INTERVAL = 30 * 60 * 1000; // 30 minutos

    /**
     * Constructor que inicializa el controlador con la interfaz de usuario.
     * 
     * @param weatherFrame La interfaz gráfica a controlar
     */
    public WeatherController(WeatherFrame weatherFrame) {
        this.weatherFrame = weatherFrame;
        
        try {
            // Verificar la API key primero
            String apiKey = ConfigManager.getApiKey();
            if (apiKey == null || apiKey.isEmpty() || 
                "your_openweathermap_api_key_here".equals(apiKey) || 
                "YOUR_API_KEY_HERE".equals(apiKey)) {
                
                weatherFrame.displayError("Se requiere una API key válida. Por favor, obtén una en openweathermap.org");
                return;
            }
            
            this.weatherService = new WeatherService();
            weatherFrame.setStatusText("Detectando tu ubicación...");
            this.locationService = new LocationService();
            setupListeners();
            setupAutoRefresh();
            
            // Iniciar la carga de datos
            weatherFrame.setStatusText("Obteniendo datos del clima...");
            fetchWeather(); 
        } catch (Exception e) {
            weatherFrame.displayError("Error al inicializar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Configura los listeners para los elementos interactivos de la interfaz.
     */
    private void setupListeners() {
        weatherFrame.getRefreshButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                weatherFrame.setStatusText("Actualizando...");
                fetchWeather();
            }
        });
    }
    
    /**
     * Configura el temporizador para actualizar automáticamente los datos.
     */
    private void setupAutoRefresh() {
        try {
            String refreshInterval = ConfigManager.getProperty("refresh.interval");
            int interval = Integer.parseInt(refreshInterval);
            autoRefreshTimer = new Timer(interval, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    weatherFrame.setStatusText("Actualizando automáticamente...");
                    fetchWeather();
                }
            });
            autoRefreshTimer.start();
        } catch (Exception e) {
            // Usar intervalo predeterminado si hay un problema
            autoRefreshTimer = new Timer(REFRESH_INTERVAL, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fetchWeather();
                }
            });
            autoRefreshTimer.start();
        }
    }

    /**
     * Obtiene los datos del clima y actualiza la interfaz.
     * Intenta primero con coordenadas, y si falla, usa el nombre de la ciudad.
     */
    private void fetchWeather() {
        try {
            // Primero intentar con coordenadas (más preciso)
            double lat = locationService.getLatitude();
            double lon = locationService.getLongitude();
            
            // Obtener el nombre de ubicación específica antes de llamar a la API
            String specificLocation = locationService.getCurrentCity();
            System.out.println("Usando ubicación específica: " + specificLocation);
            
            CurrentWeather currentWeather = weatherService.getCurrentWeatherByCoords(lat, lon);
            Forecast forecast = weatherService.getWeatherForecastByCoords(lat, lon);
            
            if (currentWeather != null && forecast != null) {
                // Sobrescribir el nombre de la ciudad con nuestra ubicación más específica
                currentWeather.setLocationName(specificLocation);
                weatherFrame.updateWeatherDisplay(currentWeather, forecast);
                return;
            }
            
            // Si las coordenadas fallan, intentar con el nombre de la ciudad
            String city = locationService.getCurrentCity();
            currentWeather = weatherService.getCurrentWeather(city);
            forecast = weatherService.getWeatherForecast(city);
            
            if (currentWeather != null || forecast != null) {
                // Asegurarnos de usar nuestra ubicación específica
                if (currentWeather != null) {
                    currentWeather.setLocationName(specificLocation);
                }
                weatherFrame.updateWeatherDisplay(currentWeather, forecast);
            } else {
                weatherFrame.displayError("No se pudieron obtener datos del clima");
            }
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            if (errorMessage.contains("API key")) {
                weatherFrame.displayError("Error de API: Se requiere una API key válida");
            } else {
                weatherFrame.displayError(errorMessage);
            }
            e.printStackTrace();
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            if (errorMessage.contains("401")) {
                weatherFrame.displayError("Error 401: API key no válida o no autorizada");
            } else if (errorMessage.contains("404")) {
                weatherFrame.displayError("Error 404: Ciudad no encontrada");
            } else if (errorMessage.contains("429")) {
                weatherFrame.displayError("Error 429: Límite de llamadas API excedido");
            } else {
                weatherFrame.displayError(errorMessage);
            }
            e.printStackTrace();
        } catch (Exception e) {
            weatherFrame.displayError(e.getMessage());
            e.printStackTrace();
        }
    }
}