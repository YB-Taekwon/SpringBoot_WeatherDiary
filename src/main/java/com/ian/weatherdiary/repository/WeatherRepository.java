package com.ian.weatherdiary.repository;

import com.ian.weatherdiary.domain.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, LocalDate> {
    List<Weather> findAllByDate(LocalDate date);
}
