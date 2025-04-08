package com.ian.weatherdiary.error;

public class DateException extends RuntimeException {
    private static final String MESSAGE = "Invalid Date.";

    public DateException() {
        super(MESSAGE);
    }
}
