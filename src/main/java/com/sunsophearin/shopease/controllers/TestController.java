package com.sunsophearin.shopease.controllers;

import com.sunsophearin.shopease.dto.SaleDto;
import com.sunsophearin.shopease.services.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    private final SaleService saleService;
    @GetMapping
    public String test(){
        return "test ";
    }
    @PostMapping("/sell")
    public ResponseEntity<String> createSales(@RequestBody SaleDto saleDto) {
        try {
//            saleService.processSale(saleDto,);
            return ResponseEntity.ok("Sale created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create sale: " + e.getMessage());
        }
    }
}
