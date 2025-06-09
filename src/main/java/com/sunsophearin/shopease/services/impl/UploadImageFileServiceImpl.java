package com.sunsophearin.shopease.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sunsophearin.shopease.services.UploadImageFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadImageFileServiceImpl implements UploadImageFileService {

    private final Cloudinary cloudinary;
    private static final String CLOUDINARY_FOLDER = "shopEase";

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        String originalFilename = Objects.requireNonNull(file.getOriginalFilename(), "Filename must not be null");
        String[] parts = splitFileName(originalFilename);
        String sanitizedName = sanitize(parts[0]);
        String extension = parts[1];
        String publicId = CLOUDINARY_FOLDER + "/" + UUID.randomUUID() + "_" + sanitizedName;

        log.debug("Uploading image with publicId: {}", publicId);

        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "public_id", publicId,
                "resource_type", "image"
        ));

        return Objects.toString(result.get("secure_url"), null);
    }

    @Override
    public List<String> uploadImages(MultipartFile[] files) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadImage(file));
        }
        return urls;
    }

    private String[] splitFileName(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1) {
            return new String[]{filename, ""}; // No extension
        }
        return new String[]{
                filename.substring(0, dotIndex),
                filename.substring(dotIndex + 1)
        };
    }

    private String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9-_]", "_");
    }
}
