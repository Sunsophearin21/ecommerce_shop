package com.sunsophearin.shopease.security.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Oauth2UserInfoDto {
    private String email;
    private String name;
    private String picture;
    private String provider;
}