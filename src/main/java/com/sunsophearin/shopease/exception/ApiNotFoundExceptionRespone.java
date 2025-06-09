package com.sunsophearin.shopease.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
@AllArgsConstructor
public class ApiNotFoundExceptionRespone {
    private HttpStatus httpStatus;
    private String message;
}
