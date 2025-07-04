package com.sunsophearin.shopease.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@RequiredArgsConstructor
public class ApiNotFoundException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;
}
