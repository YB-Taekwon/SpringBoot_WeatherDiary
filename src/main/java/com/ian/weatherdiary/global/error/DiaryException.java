package com.ian.weatherdiary.global.error;

import lombok.Getter;

@Getter
public class DiaryException extends RuntimeException {
    private final ErrorCode code;

    public DiaryException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
