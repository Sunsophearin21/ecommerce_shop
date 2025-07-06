package com.sunsophearin.shopease.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private boolean success;
    private int statusCode;
    private String code;
    private String message;
    private String details;
    private String timestamp;
}
