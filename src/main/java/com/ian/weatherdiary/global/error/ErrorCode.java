package com.ian.weatherdiary.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

    // 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다."),
    // 404
    NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),

    // Diary Error Code
    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "일치하는 정보의 일기를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
