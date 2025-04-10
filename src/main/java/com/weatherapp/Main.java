package com.weatherapp;

import com.weatherapp.ui.WeatherFrame;
import com.weatherapp.controller.WeatherController;
import com.util.ConfigManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JOptionPane;


public class Main {
    public static void main(String[] args) {
        try {
            // Establecer look and feel nativo del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Verificar API key primero para mostrar un mensaje claro
        String apiKey = ConfigManager.getApiKey();
        if (apiKey == null || apiKey.isEmpty() || 
            "your_openweathermap_api_key_here".equals(apiKey) || 
            "YOUR_API_KEY_HERE".equals(apiKey)) {
            
            JOptionPane.showMessageDialog(null, 
                "Esta aplicación requiere una API key de OpenWeatherMap.\n\n" +
                "Por favor, obtén una gratis en:\n" +
                "https://openweathermap.org/api\n\n" +
                "Luego añádela en el archivo:\n" +
                "src/main/resources/config.properties",
                "Configuración necesaria",
                JOptionPane.WARNING_MESSAGE);
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WeatherFrame weatherFrame = new WeatherFrame();
                WeatherController controller = new WeatherController(weatherFrame);
                weatherFrame.setVisible(true);
            }
        });
    }
}