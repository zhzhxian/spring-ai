package com.example.springaimcpsseserver.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WeatherServiceTest {
    @Autowired
    WeatherService weatherService;

    @Test
    void getWeather() {
        String weather = weatherService.getWeather("Beijing");
        System.out.println(weather);
    }

}