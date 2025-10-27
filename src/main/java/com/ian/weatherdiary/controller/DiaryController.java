package com.ian.weatherdiary.controller;

import com.ian.weatherdiary.dto.diary.DiaryRequestDto;
import com.ian.weatherdiary.dto.diary.DiaryResponseDto;
import com.ian.weatherdiary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diaries")
@RequiredArgsConstructor
public class DiaryController {

    public final DiaryService diaryService;

    @PostMapping("/create")
    public ResponseEntity<?> createDiary(
            @RequestBody DiaryRequestDto requestDto
    ) {
        DiaryResponseDto diary = diaryService.createDiary(requestDto);

        return ResponseEntity.ok(diary);
    }


}
