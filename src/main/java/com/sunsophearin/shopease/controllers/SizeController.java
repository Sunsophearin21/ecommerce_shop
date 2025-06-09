package com.sunsophearin.shopease.controllers;

import com.sunsophearin.shopease.dto.ColorDto;
import com.sunsophearin.shopease.dto.SizeDto;
import com.sunsophearin.shopease.services.ColorService;
import com.sunsophearin.shopease.services.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/size")
@RequiredArgsConstructor
public class SizeController {
    private final SizeService sizeService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SizeDto dto){
        return ResponseEntity.ok(sizeService.createSizw(dto));
    }
    @GetMapping("{id}")
    public ResponseEntity<?> getByid(@PathVariable Long id){
        return ResponseEntity.ok(sizeService.getSizeById(id));
    }
}
