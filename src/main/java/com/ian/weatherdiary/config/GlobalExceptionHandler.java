package com.ian.weatherdiary.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500 에러 반환
    @ExceptionHandler(Exception.class)
    public Exception handleAllException() {
        log.info("error from GlobalExceptionHandler");
        return new Exception();
    }
}
