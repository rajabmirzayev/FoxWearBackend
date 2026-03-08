package com.rgb.foxwear.controller;

import com.rgb.foxwear.dto.ApiResponse;
import com.rgb.foxwear.dto.request.catalog.CreateColorOptionRequest;
import com.rgb.foxwear.dto.response.catalog.CreateColorOptionResponse;
import com.rgb.foxwear.service.abstraction.catalog.ColorOptionService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/color-option")
@RequiredArgsConstructor
public class ColorOptionController {
    private final ColorOptionService colorOptionService;

    @PostMapping
    public ResponseEntity<@NonNull ApiResponse<CreateColorOptionResponse>> createColorOption(
            CreateColorOptionRequest createColorOptionRequest
    ) {
        var response = colorOptionService.createColorOption(createColorOptionRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
