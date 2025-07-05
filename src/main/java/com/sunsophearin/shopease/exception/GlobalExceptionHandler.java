package com.sunsophearin.shopease.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiNotFoundException.class)
    public ResponseEntity<?> handleException(ApiNotFoundException e){
        ApiNotFoundExceptionRespone apiNotFoundExceptionRespone=new ApiNotFoundExceptionRespone(e.getHttpStatus(), e.getMessage());
                return ResponseEntity.status(e.getHttpStatus()).body(apiNotFoundExceptionRespone);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse error = new ErrorResponse(
                false,
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.toString(),
                ex.getMessage(),
                null,
                Instant.now().toString()
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", ex.getMessage(),
                        "status", HttpStatus.INTERNAL_SERVER_ERROR.value()
                ));
    }
}
