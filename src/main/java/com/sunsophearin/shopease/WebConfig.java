package com.sunsophearin.shopease;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${ALLOWED_ORIGINS}")
    private String allowedOrigins;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins = allowedOrigins.split(",");
        registry.addMapping("/**")
                // ✅ Use patterns instead of exact origins if you allow credentials
                .allowedOriginPatterns(origins)
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
