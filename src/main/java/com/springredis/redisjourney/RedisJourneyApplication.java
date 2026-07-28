package com.springredis.redisjourney;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
//@EnableCaching [TODO]
public class RedisJourneyApplication{


    public static void main(String[] args) {
        SpringApplication.run(RedisJourneyApplication.class, args);
    }

}
