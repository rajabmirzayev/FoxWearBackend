package com.foxwear.dynamicdataservice.controller.admin;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.dynamicdataservice.dto.request.BannerRequest;
import com.foxwear.dynamicdataservice.dto.response.BannerResponse;
import com.foxwear.dynamicdataservice.service.DynamicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApiResponse<BannerResponse>> createBanner(
            @RequestBody @Valid BannerRequest bannerRequest
    ) {
        var response = dynamicService.createBanner(bannerRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/banner/{id}")
    @Operation(summary = "Update an existing banner")
    public ResponseEntity<ApiResponse<BannerResponse>> updateBanner(
            @RequestBody @Valid BannerRequest bannerRequest,
            @Parameter(description = "ID of the banner to update") @PathVariable Long id
    ) {
        var response = dynamicService.updateBanner(bannerRequest, id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @DeleteMapping("/banner/{id}")
    @Operation(summary = "Delete a banner")
    public ResponseEntity<ApiResponse<Void>> deleteBanner(
            @Parameter(description = "ID of the banner to delete") @PathVariable Long id
    ) {
        dynamicService.deleteBanner(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
