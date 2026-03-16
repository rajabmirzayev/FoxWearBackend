package com.rgb.foxwear.controller;

import com.rgb.foxwear.dto.ApiResponse;
import com.rgb.foxwear.dto.response.dynamic.BannerResponse;
import com.rgb.foxwear.service.DynamicService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dynamic")
@RequiredArgsConstructor
public class DynamicController {
    private final DynamicService dynamicService;

    @GetMapping("/banner")
    public ResponseEntity<@NonNull ApiResponse<BannerResponse>> getBanner(
            @RequestParam String placement
    ) {
        var response = dynamicService.getBanner(placement);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
