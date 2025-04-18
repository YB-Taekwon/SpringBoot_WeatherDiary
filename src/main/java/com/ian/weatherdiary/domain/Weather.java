package com.ian.weatherdiary.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Weather {
    @Id
    private LocalDate date;

    private String weather;
    private String icon;
    private Double temperature;
}
