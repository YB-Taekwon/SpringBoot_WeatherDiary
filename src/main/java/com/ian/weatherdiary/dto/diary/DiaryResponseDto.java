package com.ian.weatherdiary.dto.diary;

import com.ian.weatherdiary.domain.Diary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiaryResponseDto {

    private String diaryId;
    private LocalDate date;
    private String content;
    private String weather;
    private String icon;
    private Double temperature;

    public static DiaryResponseDto from(Diary diary) {
        return DiaryResponseDto.builder()
                .diaryId(diary.getDiaryId())
                .date(diary.getWeather().getDate())
                .content(diary.getContent())
                .weather(diary.getWeather().getWeather())
                .icon(diary.getWeather().getIcon())
                .temperature(diary.getWeather().getTemp())
                .build();
    }
}
