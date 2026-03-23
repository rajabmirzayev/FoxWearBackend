package com.foxwear.dynamicdataservice.controller;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.dynamicdataservice.dto.response.BannerResponse;
import com.foxwear.dynamicdataservice.service.DynamicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @GetMapping("/banner")
    @Operation(summary = "Get banner by placement", description = "Retrieves banner details based on the specified placement location.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved banner"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Banner not found for the given placement"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Banner is not active for the given placement")
    })
    public ResponseEntity<ApiResponse<BannerResponse>> getBanner(
            @Parameter(description = "Placement of the banner") @RequestParam String placement
    ) {
        var response = dynamicService.getBanner(placement);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
