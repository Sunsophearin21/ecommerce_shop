package com.sunsophearin.shopease.exception;

import org.springframework.http.HttpStatus;

public class ResoureApiNotFound extends ApiNotFoundException{

    public ResoureApiNotFound(String resoureName,Long id) {
        super(HttpStatus.NOT_FOUND,String.format("%s with id = %d Not Found",resoureName,id));
    }
}
