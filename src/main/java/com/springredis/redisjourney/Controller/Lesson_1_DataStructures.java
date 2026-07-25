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
        getSetDS();
        incrDecrDS();
        hsetHgetHgetAllDS();
    }

    private void hsetHgetHgetAllDS() {
        System.out.println(" -------- User Profile ------- ");
        hsetHgetHgetAllUserProfile();
        System.out.println(" -------- Shopping Cart ------- ");
        hsetHgetHgetAllShoppingCart();
    }

    private void hsetHgetHgetAllShoppingCart() {
        /*
        Shopping Cart using Redis Hash

        HSET cart:101 1001 2 1002 1 1003 5
        HGET cart:101 1002
        HGETALL cart:101
        */
        // HSET - Add Product to cart
        redisTemplate.opsForHash().put("cart:101", "1001", 2); // Product 1001 -> QTY 2
        redisTemplate.opsForHash().put("cart:101", "1002", 2); // Product 1002 -> QTY 2
        redisTemplate.opsForHash().put("cart:101", "1003", 5); // Product 1003 -> QTY 5
        // Get quantity of a specific product in the cart
        Object productQty = redisTemplate.opsForHash().get("cart:101", "1002");
        System.out.println("Quantity of product 1002 in cart: " + productQty);
        // Get all products and their quantities in the cart
        Object cartContents = redisTemplate.opsForHash().entries("cart:101");
        System.out.println("Cart contents: " + cartContents);
    }

    public void hsetHgetHgetAllUserProfile() {
        /*
            HSET HGET HGETALL Data Structure Redis
            HSET user:1001 name "John" email "john@example.com" login_count 1
            HGET user:1001 name
            HGETALL user:1001
         */
        redisTemplate.opsForHash().put("user:101", "name", "John");
        redisTemplate.opsForHash().put("user:101", "age", "26");
        redisTemplate.opsForHash().put("user:101", "city", "Dhaka");

        System.out.println("User profile: " + redisTemplate.opsForHash().get("user:1001", "name"));
        System.out.println("User age: " + redisTemplate.opsForHash().get("user:101", "age"));
        System.out.println("User city: " + redisTemplate.opsForHash().get("user:101", "city"));

        redisTemplate.opsForHash().entries("user:1001");
    }

    private void getSetDS() {
    /*
      get set Data Structure Redis
     */
        getSetRedis();
        // get weather info from cache
        getSetWeather("Dhaka");
    }

    private void incrDecrDS() {
    /*
        INCR DECR Data Structure Redis
     */
        incrDecrRedisDS();
        // site visitors
        siteVisitorCount();
        // rate limit
        for(int i = 0; i < 7; i++) {
            rateLimitUserPerRequest(1);
        }
    }

    public void getSetRedis() {
        redisTemplate.opsForValue().set("name1", "Rakib");
        redisTemplate.opsForValue().set("name", "Rakib", 60L, TimeUnit.SECONDS);
        System.out.println("Name from Redis: " + redisTemplate.opsForValue().get("name"));
        System.out.println("Key exists: " + redisTemplate.hasKey("name1"));
        System.out.println("Name removed from Redis: " + redisTemplate.opsForValue().getAndDelete("name1"));
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

    public void incrDecrRedisDS() {
        System.out.println("Initial value: " + redisTemplate.opsForValue().get("incrDecrRedisDS"));
        redisTemplate.opsForValue().increment("incrDecrRedisDS");
        System.out.println("Incremented value: " + redisTemplate.opsForValue().get("incrDecrRedisDS"));
    }

    public void siteVisitorCount() {
        System.out.println("Initial site visitor count: " + redisTemplate.opsForValue().get("siteVisitorCount"));
        redisTemplate.opsForValue().increment("siteVisitorCount");
        System.out.println("Updated site visitor count: " + redisTemplate.opsForValue().get("siteVisitorCount"));
    }

    // rate limit 5 request can be per second
    public void rateLimitUserPerRequest(int userId) {
        String key = "rateLimit:" + userId;
        Long currentCount = redisTemplate.opsForValue().increment(key);
        if (currentCount == 1) {
            redisTemplate.expire(key, 1, TimeUnit.SECONDS);
        }
        if (currentCount > 5) {
            System.out.println("Rate limit exceeded for user: " + userId);
        }
    }




}
