package com.weatherapp.ui;

import com.weatherapp.model.CurrentWeather;
import com.weatherapp.model.Forecast;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Normalizer;

public class WeatherFrame extends JFrame {
    private JTextArea weatherDisplay;
    private JButton refreshButton;
    private JLabel statusLabel;

    public WeatherFrame() {
        setTitle("Widget del Clima");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Hacer que la ventana parezca un widget
        setUndecorated(true);
        setAlwaysOnTop(true);
        setOpacity(0.9f);
        setBackground(new Color(0, 0, 0, 0));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        mainPanel.setBackground(new Color(240, 248, 255));
        
        // Panel superior con título y botón de actualizar
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(70, 130, 180));
        
        JLabel titleLabel = new JLabel("Mi Clima", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        refreshButton = new JButton("↻");
        refreshButton.setFocusPainted(false);
        refreshButton.setContentAreaFilled(false);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBorderPainted(false);
        refreshButton.setToolTipText("Actualizar datos del clima");
        
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(refreshButton, BorderLayout.EAST);
        
        // Display para el pronóstico
        weatherDisplay = new JTextArea();
        weatherDisplay.setEditable(false);
        weatherDisplay.setBackground(new Color(240, 248, 255));
        weatherDisplay.setFont(new Font("Arial", Font.PLAIN, 14));
        weatherDisplay.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Barra de estado para mensajes
        statusLabel = new JLabel("Obteniendo tu ubicación...");
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        statusLabel.setForeground(new Color(100, 100, 100));
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(weatherDisplay, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Permitir arrastrar el widget
        FrameDragListener frameDragListener = new FrameDragListener(this);
        addMouseListener(frameDragListener);
        addMouseMotionListener(frameDragListener);
    }

    // Clase para permitir arrastrar el widget
    private class FrameDragListener extends MouseAdapter {
        private final JFrame frame;
        private Point mouseDownCompCoords;

        public FrameDragListener(JFrame frame) {
            this.frame = frame;
        }

        public void mousePressed(MouseEvent e) {
            mouseDownCompCoords = e.getPoint();
        }

        public void mouseDragged(MouseEvent e) {
            Point currCoords = e.getLocationOnScreen();
            frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
        }
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }
    
    public void setStatusText(String text) {
        statusLabel.setText(text);
    }

    public void displayError(String message) {
        statusLabel.setText("Error: " + message);
        weatherDisplay.setText("No se pudieron obtener los datos del clima.");
    }

    public void updateWeatherDisplay(CurrentWeather currentWeather, Forecast forecast) {
        statusLabel.setText("Última actualización: " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        
        if (currentWeather == null) {
            weatherDisplay.setText("Error al obtener datos del clima");
            return;
        }

        StringBuilder displayText = new StringBuilder();
        // Capitalizar la primera letra de cada palabra de la ciudad
        String cityName = capitalizeWords(currentWeather.getCityName());
        displayText.append("Clima actual en ").append(cityName).append("\n\n");
        displayText.append("Temperatura: ").append(String.format("%.1f", currentWeather.getTemperature())).append("°C\n");
        
        // Capitalizar la primera letra de la descripción
        String description = capitalize(currentWeather.getDescription());
        displayText.append("Condición: ").append(description).append("\n");
        
        displayText.append("Humedad: ").append(String.format("%.0f", currentWeather.getHumidity())).append("%\n");
        displayText.append("Viento: ").append(String.format("%.1f", currentWeather.getWindSpeed())).append(" m/s\n\n");

        if (forecast != null && forecast.getDailyForecasts() != null && !forecast.getDailyForecasts().isEmpty()) {
            displayText.append("Próximas horas:\n");
            // Mostrar hasta 3 pronósticos para mantener el widget compacto
            int count = 0;
            for (Forecast.DailyForecast daily : forecast.getDailyForecasts()) {
                if (count++ < 3) {
                    displayText.append("• ")
                            .append(daily.getDate()).append(": ")
                            .append(String.format("%.1f", daily.getTemperature())).append("°C, ")
                            .append(capitalize(daily.getWeatherDescription())).append("\n");
                }
            }
        }

        weatherDisplay.setText(displayText.toString());
    }
    
    /**
     * Capitaliza la primera letra de una cadena
     */
    private String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
    
    /**
     * Capitaliza la primera letra de cada palabra en una cadena
     */
    private String capitalizeWords(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        StringBuilder result = new StringBuilder();
        String[] words = input.split("\\s");
        
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(word.substring(0, 1).toUpperCase());
                result.append(word.substring(1).toLowerCase());
                result.append(" ");
            }
        }
        
        return result.toString().trim();
    }
}