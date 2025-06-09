package com.sunsophearin.shopease.services;

import com.sunsophearin.shopease.dto.ResourcesDto;
import com.sunsophearin.shopease.entities.Color;
import com.sunsophearin.shopease.entities.Resources;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ResourcesService {
    Resources getById(Long id);
    Resources create(ResourcesDto dto, MultipartFile[] files) throws IOException;
}
