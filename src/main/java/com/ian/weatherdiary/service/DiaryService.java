package com.ian.weatherdiary.service;

import com.ian.weatherdiary.domain.Diary;
import com.ian.weatherdiary.domain.Weather;
import com.ian.weatherdiary.dto.diary.DiaryRequestDto;
import com.ian.weatherdiary.dto.diary.DiaryResponseDto;
import com.ian.weatherdiary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final WeatherService weatherService;

    public DiaryResponseDto createDiary(DiaryRequestDto requestDto) {
        Weather weather = weatherService.getWeather(requestDto.getDate());

        Diary diary = diaryRepository.save(
                Diary.builder()
                        .content(requestDto.getContent())
                        .weather(weather)
                        .build()
        );

        return DiaryResponseDto.from(diary);
    }
}
