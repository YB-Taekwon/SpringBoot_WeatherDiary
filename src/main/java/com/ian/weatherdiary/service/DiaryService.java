package com.ian.weatherdiary.service;

import com.ian.weatherdiary.domain.Diary;
import com.ian.weatherdiary.domain.Weather;
import com.ian.weatherdiary.dto.DiaryEditRequestDto;
import com.ian.weatherdiary.dto.diary.DiaryRequestDto;
import com.ian.weatherdiary.dto.diary.DiaryResponseDto;
import com.ian.weatherdiary.dto.diary.DiarySearchDto;
import com.ian.weatherdiary.global.error.DiaryException;
import com.ian.weatherdiary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.ian.weatherdiary.global.error.ErrorCode.DIARY_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final WeatherService weatherService;

    @Transactional
    public DiaryResponseDto createDiary(DiaryRequestDto requestDto) {
        long start = System.nanoTime();

        String diaryId = generateDiaryId();
        MDC.put("diaryId", diaryId);

        try {
            if (log.isDebugEnabled())
                log.debug("createDiary 실행: date={}", requestDto.getDate());

            Weather weather = weatherService.getWeather(requestDto.getDate());

            if (log.isDebugEnabled())
                log.debug("weather 반환: weather={}", weather.getWeather());

            Diary diary = diaryRepository.save(
                    Diary.builder()
                            .diaryId(diaryId)
                            .content(requestDto.getContent())
                            .weather(weather)
                            .build()
            );

            log.info("일기 생성 완료: id={}, weather={}", diary.getDiaryId(), weather.getWeather());
            return DiaryResponseDto.from(diary);
        } finally {
            long tookMs = java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

            if (log.isDebugEnabled())
                log.debug("createDiary 종료: id={}, took={}ms", diaryId, tookMs);

            MDC.remove("diaryId");
        }
    }

    public Page<DiaryResponseDto> getDiaries(DiarySearchDto requestDto) {
        long start = System.nanoTime();
        Pageable pageable = requestDto.toPageable();

        try {
            if (log.isDebugEnabled()) {
                log.debug("getDiaries 실행: page={}, size={}",
                        pageable.getPageNumber(), pageable.getPageSize());
            }

            Page<Diary> diaries = diaryRepository.searchDiaries(requestDto, pageable);
            log.info("Diaries 조회: totalElements={}", diaries.getTotalElements());

            return diaries.map(DiaryResponseDto::from);
        } finally {
            long tookMs = java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

            if (log.isDebugEnabled())
                log.debug("getDiaries 종료: took={}ms", tookMs);
        }
    }

    public DiaryResponseDto getDiary(String diaryId) {
        long start = System.nanoTime();
        MDC.put("diaryId", diaryId);

        try {
            if (log.isDebugEnabled())
                log.debug("getDiary 실행: id={}", diaryId);

            Diary diary = findDiaryByDiaryId(diaryId);
            log.info("Diary 조회: id={}", diary.getDiaryId());

            return DiaryResponseDto.from(diary);
        } finally {
            long tookMs = java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

            if (log.isDebugEnabled())
                log.debug("getDiary 종료: took={}ms", tookMs);

            MDC.remove("diaryId");
        }
    }

    @Transactional
    public DiaryResponseDto updateDiary(String diaryId, DiaryEditRequestDto requestDto) {
        long start = System.nanoTime();
        MDC.put("diaryId", diaryId);

        try {
            Diary diary = findDiaryByDiaryId(diaryId);
            diary.updateContent(requestDto.getContent());
            log.info("Diary 수정: id={}", diary.getDiaryId());

            return DiaryResponseDto.from(diary);
        } finally {
            long tookMs = java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

            if (log.isDebugEnabled())
                log.debug("updateDiary 종료: took={}ms", tookMs);

            MDC.remove("diaryId");
        }
    }

    @Transactional
    public void deleteDiary(String diaryId) {
        long start = System.nanoTime();
        MDC.put("diaryId", diaryId);

        try {
            Diary diary = findDiaryByDiaryId(diaryId);
            diaryRepository.delete(diary);
            log.info("Diary 삭제");
        } finally {
            long tookMs = java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

            if (log.isDebugEnabled())
                log.debug("deleteDiary 종료: took={}ms", tookMs);

            MDC.remove("diaryId");
        }
    }

    private String generateDiaryId() {
        return UUID.randomUUID().toString();
    }

    private Diary findDiaryByDiaryId(String diaryId) {
        return diaryRepository.findByDiaryId(diaryId)
                .orElseThrow(() -> new DiaryException(DIARY_NOT_FOUND));
    }
}
