package com.sunsophearin.shopease.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String error;
    private final int status;
    private final String statusName;

    public ApiError(String error, HttpStatus status) {
        this.error = error;
        this.status = status.value();
        this.statusName = status.name();
    }
}