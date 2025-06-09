package com.sunsophearin.shopease.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConfigCloudinary {
    @Bean
    public Cloudinary configKey(){
        Map<String,String>config=new HashMap<>();
        config.put("cloud_name", "dwldabifd");
        config.put("api_key", "367719439942324");
        config.put("api_secret", "wWrOpMRFl9cSvZg6N1PhDQ-h4hU");
        return new Cloudinary(config);
    }
}
