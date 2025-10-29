package com.ian.weatherdiary.service;

import com.ian.weatherdiary.domain.Diary;
import com.ian.weatherdiary.domain.Weather;
import com.ian.weatherdiary.dto.DiaryEditRequestDto;
import com.ian.weatherdiary.dto.diary.DiaryRequestDto;
import com.ian.weatherdiary.dto.diary.DiaryResponseDto;
import com.ian.weatherdiary.dto.diary.DiarySearchDto;
import com.ian.weatherdiary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final WeatherService weatherService;

    @Transactional
    public DiaryResponseDto createDiary(DiaryRequestDto requestDto) {
        Weather weather = weatherService.getWeather(requestDto.getDate());
        String diaryId = generateDiaryId();

        Diary diary = diaryRepository.save(
                Diary.builder()
                        .diaryId(diaryId)
                        .content(requestDto.getContent())
                        .weather(weather)
                        .build()
        );

        return DiaryResponseDto.from(diary);
    }

    public Page<DiaryResponseDto> getDiaries(DiarySearchDto requestDto) {
        Pageable pageable = requestDto.toPageable();
        return diaryRepository.searchDiaries(requestDto, pageable)
                .map(DiaryResponseDto::from);
    }

    public DiaryResponseDto getDiary(String diaryId) {
        Diary diary = findDiaryByDiaryId(diaryId);

        return DiaryResponseDto.from(diary);
    }

    @Transactional
    public DiaryResponseDto updateDiary(String diaryId, DiaryEditRequestDto requestDto) {
        Diary diary = findDiaryByDiaryId(diaryId);
        diary.updateContent(requestDto.getContent());

        return DiaryResponseDto.from(diary);
    }

    @Transactional
    public void deleteDiary(String diaryId) {
        Diary diary = findDiaryByDiaryId(diaryId);
        diaryRepository.delete(diary);
    }

    private String generateDiaryId() {
        return UUID.randomUUID().toString();
    }

    private Diary findDiaryByDiaryId(String diaryId) {
        return diaryRepository.findByDiaryId(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 일기를 찾을 수 없습니다."));
    }
}
