package com.ian.weatherdiary.repository;

import com.ian.weatherdiary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long>, DiaryRepositoryCustom {
    Optional<Diary> findByDiaryId(String diaryId);
}
