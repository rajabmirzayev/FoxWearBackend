package com.rgb.foxwear.controller;

import com.rgb.foxwear.dto.ApiResponse;
import com.rgb.foxwear.dto.response.dynamic.BannerResponse;
import com.rgb.foxwear.service.DynamicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Dynamic Management", description = "APIs for getting dynamic contents like banners")
public class DynamicController {
    private final DynamicService dynamicService;

    @Operation(summary = "Get banner by placement")
    @GetMapping("/banner")
    public ResponseEntity<@NonNull ApiResponse<BannerResponse>> getBanner(
            @Parameter(description = "Placement of the banner") @RequestParam String placement
    ) {
        var response = dynamicService.getBanner(placement);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
