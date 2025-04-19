package com.scm.Services;

public interface WeatherService {
    public String getMoodBasedOnTime();
    public String getCurrentWeather(String city);
    // Removed invalid private method declaration
    public int getDailyProgress(String userId);
}
