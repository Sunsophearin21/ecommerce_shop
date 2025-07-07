package com.sunsophearin.shopease.controllers;
import com.sunsophearin.shopease.dto.DeliveryFeeDto;

import com.sunsophearin.shopease.services.DeliveryFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/delivery-fee")
@RequiredArgsConstructor
public class DeliveryFeeController {
    private final DeliveryFeeService deliveryFeeService;

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @ModelAttribute DeliveryFeeDto dto,
            @RequestParam MultipartFile file
    ) throws IOException {
        return ResponseEntity.ok(deliveryFeeService.createDeliveryFee(dto, file));
    }
}

