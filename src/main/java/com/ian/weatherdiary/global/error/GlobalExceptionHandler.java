package com.ian.weatherdiary.global.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.ian.weatherdiary.global.error.ErrorCode.*;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiaryException.class)
    public ResponseEntity<ErrorResponse> handleDiaryException(
            DiaryException e, HttpServletRequest request
    ) {
        return ResponseEntity.status(e.getCode().getHttpStatus())
                .body(ErrorResponse.of(e.getCode()));
    }

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(
            Exception e, HttpServletRequest request
    ) {
        log.error("[400] path={}, msg={}", request.getRequestURI(), e.getMessage(), e);

        return ResponseEntity.status(BAD_REQUEST.getHttpStatus())
                .body(ErrorResponse.of(BAD_REQUEST));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoHandlerFoundException e, HttpServletRequest request) {
        log.error("[404] path={}, msg={}", request.getRequestURI(), e.getMessage());

        return ResponseEntity.status(NOT_FOUND.getHttpStatus())
                .body(ErrorResponse.of(NOT_FOUND));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("[500] path={}, msg={}", request.getRequestURI(), e.getMessage(), e);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(ErrorResponse.of(INTERNAL_SERVER_ERROR));
    }
}
