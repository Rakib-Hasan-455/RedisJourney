package com.springredis.redisjourney.Controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class Lesson_1_DataStructures {

    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        // Initialize the RedisTemplate
        getSetWeather("Dhaka");
    }

    public void getSetRedis() {
        redisTemplate.opsForValue().set("name1", "Rakib");
        redisTemplate.opsForValue().set("name", "Rakib", 60L, TimeUnit.SECONDS);
        System.out.println("Name from Redis: " + redisTemplate.opsForValue().get("name"));
    }

    // let's implement weather api with redis caching
    public void getSetWeather(String city) {
        // Check if the weather data is already in Redis
        String weatherData = (String) redisTemplate.opsForValue().get("weather:" + city);
        if (weatherData == null) {
            // If not in Redis, fetch from the weather API and store in Redis
            // This is a placeholder for the actual API call
            weatherData = fetchWeatherFromAPI(city);
            redisTemplate.opsForValue().set("weather:" + city, weatherData, 60L, TimeUnit.SECONDS); // Cache for 1 minutes
        }
        System.out.println(weatherData);
    }

    private String fetchWeatherFromAPI(String city) {
        // This is a placeholder for the actual API call
        return "Weather data for " + city + " time " + java.time.LocalDateTime.now();
    }
}
