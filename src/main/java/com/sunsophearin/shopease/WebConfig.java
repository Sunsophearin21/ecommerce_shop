package com.sunsophearin.shopease;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // ✅ Use patterns instead of exact origins if you allow credentials
                .allowedOriginPatterns("http://localhost:3000", "http://localhost:3001")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
