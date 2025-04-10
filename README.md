# Weather App

This is a Java application that displays the current weather and forecast for a specified city using a graphical user interface built with Swing. The application fetches weather data from the OpenWeatherMap API.

## Project Structure

```
weather-app
├── src
│   └── main
│       ├── java
│       │   └── com
│       │       ├── weatherapp
│       │       │   ├── Main.java
│       │       │   ├── controller
│       │       │   │   └── WeatherController.java
│       │       │   ├── model
│       │       │   │   ├── CurrentWeather.java
│       │       │   │   └── Forecast.java
│       │       │   ├── service
│       │       │   │   └── WeatherService.java
│       │       │   └── ui
│       │       │       └── WeatherFrame.java
│       │       └── util
│       │           ├── ApiConnector.java
│       │           └── ConfigManager.java
│       └── resources
│           └── config.properties
├── pom.xml
└── README.md
```

## Setup Instructions

1. **Clone the repository**:
   ```
   git clone <repository-url>
   cd weather-app
   ```

2. **Configure the API Key**:
   - Open the `src/main/resources/config.properties` file.
   - Add your OpenWeatherMap API key:
     ```
     api.key=YOUR_API_KEY
     ```

3. **Build the Project**:
   - Ensure you have Maven installed.
   - Run the following command to build the project:
     ```
     mvn clean install
     ```

4. **Run the Application**:
   - Execute the main class:
     ```
     mvn exec:java -Dexec.mainClass="com.weatherapp.Main"
     ```

## Usage

- Upon launching the application, enter the name of the city for which you want to see the weather.
- The application will display the current weather conditions and a forecast for the next few days.

## Dependencies

- Java 8 or higher
- Maven

## License

This project is licensed under the MIT License. See the LICENSE file for details.