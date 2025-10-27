package com.ian.weatherdiary.service;

import com.ian.weatherdiary.domain.Weather;
import com.ian.weatherdiary.dto.weather.OpenWeatherResponseDto;
import com.ian.weatherdiary.repository.WeatherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final WeatherRepository weatherRepository;

    private static final String DEFAULT_CITY = "seoul";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Transactional
    public Weather getWeather(LocalDate date) {
        return weatherRepository.findByDate(date)
                .orElseGet(() -> saveWeather(date));

    }

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void saveDailyWeather() {
        Weather weather = buildWeather();
        weather.updateDate(LocalDate.now());

        weatherRepository.save(weather);
    }

    private Weather saveWeather(LocalDate date) {
        Weather weather = buildWeather();
        weather.updateDate(date);

        return weatherRepository.save(weather);
    }

    private Weather buildWeather() {
        OpenWeatherResponseDto weatherDto = requestOpenWeather();

        String weather = weatherDto.getWeather().getFirst().getMain();
        String icon = weatherDto.getWeather().getFirst().getIcon();
        Double temp = weatherDto.getMain().getTemp();

        return Weather.builder()
                .weather(weather)
                .icon(icon)
                .temp(temp)
                .build();
    }

    private OpenWeatherResponseDto requestOpenWeather() {
        URI uri = UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam("q", DEFAULT_CITY)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .build()
                .toUri();

        return restTemplate.getForObject(uri, OpenWeatherResponseDto.class);
    }
}
