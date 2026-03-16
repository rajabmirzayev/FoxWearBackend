package com.rgb.foxwear.controller.admin;

import com.rgb.foxwear.dto.ApiResponse;
import com.rgb.foxwear.dto.request.dynamic.BannerRequest;
import com.rgb.foxwear.dto.response.dynamic.BannerResponse;
import com.rgb.foxwear.service.DynamicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dynamic")
@RequiredArgsConstructor
@Tag(name = "Admin Dynamic Management", description = "APIs for managing dynamic content like banners")
public class AdminDynamicController {
    private final DynamicService dynamicService;

    @PostMapping("/banner")
    @Operation(summary = "Create a new banner")
    public ResponseEntity<@NonNull ApiResponse<BannerResponse>> createBanner(
            @RequestBody @Valid BannerRequest bannerRequest
    ) {
        var response = dynamicService.createBanner(bannerRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/banner/{id}")
    @Operation(summary = "Update an existing banner")
    public ResponseEntity<@NonNull ApiResponse<BannerResponse>> updateBanner(
            @RequestBody @Valid BannerRequest bannerRequest,
            @Parameter(description = "ID of the banner to update") @PathVariable Long id
    ) {
        var response = dynamicService.updateBanner(bannerRequest, id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @DeleteMapping("/banner/{id}")
    @Operation(summary = "Delete a banner")
    public ResponseEntity<@NonNull ApiResponse<Void>> deleteBanner(
            @Parameter(description = "ID of the banner to delete") @PathVariable Long id
    ) {
        dynamicService.deleteBanner(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
