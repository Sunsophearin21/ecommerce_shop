package com.sunsophearin.shopease.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobleException {
    @ExceptionHandler(ApiNotFoundException.class)
    public ResponseEntity<?> handleException(ApiNotFoundException e){
        ApiNotFoundExceptionRespone apiNotFoundExceptionRespone=new ApiNotFoundExceptionRespone(e.getHttpStatus(), e.getMessage());
                return ResponseEntity.status(e.getHttpStatus()).body(apiNotFoundExceptionRespone);
    }
}
