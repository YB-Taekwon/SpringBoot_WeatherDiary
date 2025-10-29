package com.ian.weatherdiary.repository;

import com.ian.weatherdiary.domain.Diary;
import com.ian.weatherdiary.dto.diary.DiarySearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiaryRepositoryCustom {

    Page<Diary> searchDiaries(DiarySearchDto request, Pageable pageable);
}
