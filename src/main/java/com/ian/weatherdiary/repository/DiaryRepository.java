package com.ian.weatherdiary.repository;

import com.ian.weatherdiary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    // 특정 날짜 일기 조회
    List<Diary> findAllByDate(LocalDate date);

    // 범위 내 날짜의 일기 조회
    List<Diary> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
}
