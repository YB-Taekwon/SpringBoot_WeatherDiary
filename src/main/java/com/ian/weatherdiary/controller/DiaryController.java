package com.ian.weatherdiary.controller;

import com.ian.weatherdiary.dto.DiaryEditRequestDto;
import com.ian.weatherdiary.dto.diary.DiaryRequestDto;
import com.ian.weatherdiary.dto.diary.DiaryResponseDto;
import com.ian.weatherdiary.dto.diary.DiarySearchDto;
import com.ian.weatherdiary.service.DiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diaries")
@RequiredArgsConstructor
public class DiaryController {

    public final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<?> createDiary(
            @RequestBody DiaryRequestDto requestDto
    ) {
        DiaryResponseDto diary = diaryService.createDiary(requestDto);

        return ResponseEntity.ok(diary);
    }

    @PostMapping("/search")
    public ResponseEntity<?> getDiaries(@RequestBody DiarySearchDto requestDto) {
        return ResponseEntity.ok(diaryService.getDiaries(requestDto));
    }

    @PatchMapping("/{diaryId}/content")
    public ResponseEntity<?> updateDiary(
            @PathVariable String diaryId,
            @Valid @RequestBody DiaryEditRequestDto requestDto
    ) {
        return ResponseEntity.ok(diaryService.updateDiary(diaryId, requestDto));
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<?> deleteDiary(@PathVariable String diaryId) {
        diaryService.deleteDiary(diaryId);

        return ResponseEntity.ok("일기가 성공적으로 삭제되었습니다.");
    }
}
