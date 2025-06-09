package com.sunsophearin.shopease.services.impl;

import com.sunsophearin.shopease.dto.ResourcesDto;
import com.sunsophearin.shopease.entities.Resources;
import com.sunsophearin.shopease.exception.ResoureApiNotFound;
import com.sunsophearin.shopease.mapper.ResourcesMapper;
import com.sunsophearin.shopease.repositories.ResourcesRepository;
import com.sunsophearin.shopease.services.ResourcesService;
import com.sunsophearin.shopease.services.UploadImageFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourcesServiceimpl implements ResourcesService {
    private final ResourcesRepository resourcesRepository;
    private final ResourcesMapper resourcesMapper;
    private final UploadImageFileService uploadImageFileService;

    @Override
    public Resources getById(Long id) {
        return resourcesRepository.findById(id).orElseThrow(()->new ResoureApiNotFound("Resoure",id));
    }

    @Override
    public Resources create(ResourcesDto dto, MultipartFile[] files) throws IOException {
        List<String> urls = uploadImageFileService.uploadImages(files);

        Resources resources = resourcesMapper.toEntity(dto);

        // assuming you changed Resources.java to store List<String> or List<Image>
        resources.setImages(urls);

        return resourcesRepository.save(resources);
    }

}
