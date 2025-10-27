package com.ian.weatherdiary.controller;

import com.ian.weatherdiary.dto.weather.WeatherDateRequestDto;
import com.ian.weatherdiary.service.WeatherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/weathers")
public class WeatherController {

    private final WeatherService weatherService;

    @PostMapping
    public ResponseEntity<?> getWeather(
            @Valid @RequestBody WeatherDateRequestDto requestDto
    ) {
        return ResponseEntity.ok(weatherService.getWeather(requestDto.getDate()));
    }
}
