package com.sunsophearin.shopease.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunsophearin.shopease.dto.DeliveryFeeDto;
import com.sunsophearin.shopease.dto.ProductVariantDto;
import com.sunsophearin.shopease.mapper.DeliveryFeMapper;
import com.sunsophearin.shopease.services.DeliveryFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
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

