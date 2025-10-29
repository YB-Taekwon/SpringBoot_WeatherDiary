package com.ian.weatherdiary.controller;

import com.ian.weatherdiary.dto.DiaryEditRequestDto;
import com.ian.weatherdiary.dto.diary.DiaryRequestDto;
import com.ian.weatherdiary.dto.diary.DiarySearchDto;
import com.ian.weatherdiary.service.DiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/diaries")
@RequiredArgsConstructor
public class DiaryController {

    public final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<?> createDiary(
            @RequestBody DiaryRequestDto requestDto
    ) {
        log.info("[POST /diaries] - createDiary called");
        return ResponseEntity.ok(diaryService.createDiary(requestDto));
    }

    @PostMapping("/search")
    public ResponseEntity<?> getAllDiaries(@RequestBody DiarySearchDto requestDto) {
        log.info("[POST /diaries/search] - getAllDiaries called");
        return ResponseEntity.ok(diaryService.getDiaries(requestDto));
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<?> getDiary(@PathVariable String diaryId) {
        log.info("[GET /{diaryId}] - getDiary called");
        return ResponseEntity.ok(diaryService.getDiary(diaryId));
    }

    @PatchMapping("/{diaryId}/content")
    public ResponseEntity<?> updateDiary(
            @PathVariable String diaryId,
            @Valid @RequestBody DiaryEditRequestDto requestDto
    ) {
        log.info("[PATCH /{diaryId}/content] - updateDiary called");
        return ResponseEntity.ok(diaryService.updateDiary(diaryId, requestDto));
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<?> deleteDiary(@PathVariable String diaryId) {
        log.info("[DELETE /{diaryId}] - deleteDiary called");
        diaryService.deleteDiary(diaryId);

        return ResponseEntity.ok("일기가 성공적으로 삭제되었습니다.");
    }
}
