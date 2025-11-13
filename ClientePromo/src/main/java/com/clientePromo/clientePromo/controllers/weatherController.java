package com.clientePromo.clientePromo.controllers;

import com.clientePromo.clientePromo.models.WeatherResponse;
import com.clientePromo.clientePromo.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/weather")
public class weatherController {

    @Autowired
    private final WeatherService weatherService;

    public weatherController(final WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public Mono<WeatherResponse> getWeather(@RequestParam String city){
        return weatherService.getWeatherByCity(city);
    }

}
