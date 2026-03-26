package com.foxwear.interactionservice.controller.admin;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.interactionservice.service.ReviewService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
@Tag(name = "Admin Review Controller", description = "Management APIs for product reviews")
public class AdminReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "Toggle review activation status", description = "Activates or deactivates a review by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully toggled review status"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Review not found")
    })
    @PatchMapping("/site/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> patchString(
            @Parameter(description = "ID of the review to be toggled", required = true) @PathVariable Long id
    ) {
        var response = reviewService.activateReview(id);
        return ResponseEntity.ok(ApiResponse.success(null, response ? "Activated" : "Deactivated"));
    }

}
