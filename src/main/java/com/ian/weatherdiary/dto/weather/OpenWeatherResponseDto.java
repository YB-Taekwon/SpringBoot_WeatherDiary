package com.ian.weatherdiary.dto.weather;

import lombok.Getter;

import java.util.List;

@Getter
public class OpenWeatherResponseDto {

    private List<WeatherDto> weather;
    private MainDto main;

    @Getter
    public static class WeatherDto {
        private String main;
        private String icon;
    }

    @Getter
    public static class MainDto {
        private Double temp;
    }
}
