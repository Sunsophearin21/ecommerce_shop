package com.sunsophearin.shopease.controllers;

import com.sunsophearin.shopease.dto.ColorDto;
import com.sunsophearin.shopease.dto.MenuFactureDto;
import com.sunsophearin.shopease.services.ColorService;
import com.sunsophearin.shopease.services.MenuFacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/menufacture")
@RequiredArgsConstructor
public class MenuFacturerController {
    private final MenuFacturerService menuFacturerService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MenuFactureDto dto){
        return ResponseEntity.ok(menuFacturerService.create(dto));
    }
    @GetMapping("{id}")
    public ResponseEntity<?> getByid(@PathVariable Long id){
        return ResponseEntity.ok(menuFacturerService.getMenuFacById(id));
    }
}
