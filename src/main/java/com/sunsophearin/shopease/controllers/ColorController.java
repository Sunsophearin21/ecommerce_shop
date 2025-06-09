package com.sunsophearin.shopease.controllers;

import com.sunsophearin.shopease.dto.ColorDto;
import com.sunsophearin.shopease.services.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/color")
@RequiredArgsConstructor
public class ColorController {
    private final ColorService colorService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ColorDto dto){
        return ResponseEntity.ok(colorService.createColor(dto));
    }
    @GetMapping("{id}")
    public ResponseEntity<?> getByid(@PathVariable Long id){
        return ResponseEntity.ok(colorService.getColorById(id));
    }
}
