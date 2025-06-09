package com.sunsophearin.shopease.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UploadImageFileService {
    String uploadImage(MultipartFile file) throws IOException;
    List<String> uploadImages(MultipartFile[] files) throws IOException;
}
