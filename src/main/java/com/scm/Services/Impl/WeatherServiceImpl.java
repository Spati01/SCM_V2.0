package com.scm.Services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.scm.Services.WeatherService;

import java.time.LocalDate;
import java.time.LocalTime;

import org.json.JSONException;
import org.json.JSONObject;

import com.scm.Repository.ContactRepo;


@Service
public class WeatherServiceImpl implements WeatherService{

    @Value("${openweather.api.key}")
    private String apiKey;

    @Autowired
    private ContactRepo contactRepository;

        @Override
     public String getMoodBasedOnTime() {
        LocalTime now = LocalTime.now();

        if (now.isBefore(LocalTime.NOON)) {
            return "🌞 Energetic";
        } else if (now.isBefore(LocalTime.of(17, 0))) {
            return "📈 Productive";
        } else if (now.isBefore(LocalTime.of(21, 0))) {
            return "🌇 Reflective";
        } else {
            return "🌙 Winding Down";
        }
    }
@Override
public String getCurrentWeather(String city) {
    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=" + apiKey;

    RestTemplate restTemplate = new RestTemplate();
    try {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        JSONObject json = new JSONObject(response.getBody());

        double temp = json.getJSONObject("main").getDouble("temp");
        String weatherDesc = json.getJSONArray("weather").getJSONObject(0).getString("description");

        return String.format("%.0f°C, %s", temp, capitalize(weatherDesc));
    } catch (HttpClientErrorException | HttpServerErrorException e) {
        // API returned an error response (e.g., 404 or 500)
        System.err.println("Weather API error: " + e.getMessage());
    } catch (ResourceAccessException e) {
        // Connection failure
        System.err.println("Weather API unreachable: " + e.getMessage());
    } catch (JSONException e) {
        // Parsing error
        System.err.println("Invalid weather data format: " + e.getMessage());
    }

    return "Unavailable";
}


    private String capitalize(String text) {
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }


    @Override
    public int getDailyProgress(String userId) {
    int goal = 10; // You can move this to config
    int addedToday = contactRepository.countByUserAndDate(userId, LocalDate.now());

    return Math.min((int) ((addedToday / (double) goal) * 100), 100);
}


}
