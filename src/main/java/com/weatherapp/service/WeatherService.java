package com.weatherapp.service;

import com.weatherapp.model.CurrentWeather;
import com.weatherapp.model.Forecast;
import com.util.ApiConnector;
import com.util.ConfigManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherService {
    private final ApiConnector apiConnector;

    public WeatherService() {
        String apiKey = ConfigManager.getApiKey();
        this.apiConnector = new ApiConnector(apiKey);
    }

    public CurrentWeather getCurrentWeather(String city) {
        try {
            String response = apiConnector.getCurrentWeatherByCity(city);
            return parseCurrentWeather(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public CurrentWeather getCurrentWeatherByCoords(double lat, double lon) {
        try {
            String response = apiConnector.getCurrentWeatherByCoords(lat, lon);
            return parseCurrentWeather(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Forecast getWeatherForecast(String city) {
        try {
            String response = apiConnector.getForecastByCity(city);
            return parseForecast(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Forecast getWeatherForecastByCoords(double lat, double lon) {
        try {
            String response = apiConnector.getForecastByCoords(lat, lon);
            return parseForecast(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private CurrentWeather parseCurrentWeather(String jsonResponse) {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            
            String cityName = json.getString("name");
            
            JSONObject mainData = json.getJSONObject("main");
            double temperature = mainData.getDouble("temp");
            double humidity = mainData.getDouble("humidity");
            
            JSONObject windData = json.getJSONObject("wind");
            double windSpeed = windData.getDouble("speed");
            
            JSONArray weatherArray = json.getJSONArray("weather");
            String description = "";
            if (weatherArray.length() > 0) {
                JSONObject weatherData = weatherArray.getJSONObject(0);
                description = weatherData.getString("description");
            }
            
            return new CurrentWeather(cityName, description, temperature, humidity, windSpeed);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Forecast parseForecast(String jsonResponse) {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            
            JSONObject city = json.getJSONObject("city");
            String cityName = city.getString("name");
            
            JSONArray forecastList = json.getJSONArray("list");
            List<Forecast.DailyForecast> dailyForecasts = new ArrayList<>();
            
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, dd MMM");
            
            // Solo procesamos los datos para las próximas 24-48 horas (para el día siguiente)
            // y a intervalos de 8 horas para no saturar el widget
            for (int i = 0; i < Math.min(3, forecastList.length()); i++) {
                JSONObject timeSlot = forecastList.getJSONObject(i);
                
                String dateText = timeSlot.getString("dt_txt");
                Date date = inputFormat.parse(dateText);
                String formattedDate = outputFormat.format(date);
                
                JSONObject mainData = timeSlot.getJSONObject("main");
                double temperature = mainData.getDouble("temp");
                
                JSONArray weatherArray = timeSlot.getJSONArray("weather");
                String description = "";
                if (weatherArray.length() > 0) {
                    description = weatherArray.getJSONObject(0).getString("description");
                }
                
                dailyForecasts.add(new Forecast.DailyForecast(formattedDate, temperature, description));
            }
            
            return new Forecast(cityName, dailyForecasts);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}