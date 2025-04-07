package com.ian.weatherdiary.controller;

import com.ian.weatherdiary.domain.Diary;
import com.ian.weatherdiary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

    /**
     * 일기 작성 API
     * 파라미터: 작성일, 작성 내용
     * 날짜 포맷 형식: yyyy-MM-dd
     */
    @PostMapping("/create/diary")
    void createDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestBody String text) {
        diaryService.createDiary(date, text);
    }


    /**
     * 특정 날짜 일기 조회 API
     * 파라미터: 작성일
     * 날짜 포맷 형식: yyyy-MM-dd
     */
    @GetMapping("read/diary")
    List<Diary> readDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return diaryService.readDiary(date);
    }


    /**
     * 범위 내 날짜의 일기 조회 API
     * 파라미터: 범위 시작일, 범위 마감일
     * 날짜 범위: startDate ~ endDate 사이의 날짜
     */
    @GetMapping("read/diaries")
    List<Diary> readDiaries(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return diaryService.readDiaries(startDate, endDate);
    }

}
