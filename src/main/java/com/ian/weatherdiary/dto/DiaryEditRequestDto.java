package com.ian.weatherdiary.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DiaryEditRequestDto {

    @NotBlank
    private String content;
}
