package com.ian.weatherdiary.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String weather;
    private String icon;
    private Double temperature;
    private String text;
    private LocalDate date;


    public void setDateWeather(Weather weather) {
        this.date = weather.getDate();
        this.weather = weather.getWeather();
        this.icon = weather.getIcon();
        this.temperature = weather.getTemperature();
    }
}
