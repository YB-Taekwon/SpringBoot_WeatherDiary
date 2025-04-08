package com.ian.weatherdiary.controller;

import com.ian.weatherdiary.domain.Diary;
import com.ian.weatherdiary.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(summary = "일기 텍스트와 날씨를 이용해서 DB에 일기를 저장합니다.")
    @PostMapping("/create/diary")
    void createDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestBody String text) {
        diaryService.createDiary(date, text);
    }


    /**
     * 특정 날짜 일기 조회 API
     * 파라미터: 작성일
     * 날짜 포맷 형식: yyyy-MM-dd
     */
    @Operation(summary = "선택한 날짜의 모든 일기 데이터를 가져옵니다.")
    @GetMapping("read/diary")
    List<Diary> readDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return diaryService.readDiary(date);
    }


    /**
     * 범위 내 날짜의 일기 조회 API
     * 파라미터: 범위 시작일, 범위 마감일
     * 날짜 범위: startDate ~ endDate 사이의 날짜
     * 날짜 포맷 형식: yyyy-MM-dd
     */
    @Operation(summary = "선택한 기간 내의 모든 일기 데이터를 가져옵니다.")
    @GetMapping("read/diaries")
    List<Diary> readDiaries(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "조회할 기간의 첫번째 날", example = "2025-01-01") LocalDate startDate,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "조회할 기간의 마지막 날", example = "2025-04-08") LocalDate endDate) {
        return diaryService.readDiaries(startDate, endDate);
    }


    /**
     * 일기 수정 API
     * 파라미터: 작성일, 수정 내용
     * 날짜 포맷 형식: yyyy-MM-dd
     */
    @PutMapping("/update/diary")
    void updateDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestBody String text) {
        diaryService.updateDiary(date, text);
    }


    /**
     * 일기 삭제 API
     * 파라미터: 작성일
     * 날짜 포맷 형식: yyyy-MM-dd
     */
    @DeleteMapping("/delete/diary")
    void deleteDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        diaryService.deleteDiary(date);
    }
}
