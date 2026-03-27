package com.foxwear.interactionservice.controller;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.interactionservice.dto.request.SiteReviewCreateRequest;
import com.foxwear.interactionservice.dto.request.SiteReviewUpdateRequest;
import com.foxwear.interactionservice.dto.response.SiteReviewGetAllResponse;
import com.foxwear.interactionservice.dto.response.SiteReviewCreateResponse;
import com.foxwear.interactionservice.dto.response.SiteReviewUpdateResponse;
import com.foxwear.interactionservice.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Review Controller", description = "Endpoints for managing and retrieving site reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/site")
    @Operation(
            summary = "Create a site review",
            description = "Allows a user to submit a new review for the site."
    )
    public ResponseEntity<ApiResponse<SiteReviewCreateResponse>> createSiteReview(
            @Valid @RequestBody SiteReviewCreateRequest siteReviewCreateRequest,
            @Parameter(description = "ID of the user creating the review", required = true) @RequestHeader(value = "X-User-Id") Long userId
    ) {
        var response = reviewService.createSiteReview(siteReviewCreateRequest, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/site")
    @Operation(
            summary = "Get all site reviews",
            description = "Retrieves a paginated list of all site reviews."
    )
    public ResponseEntity<ApiResponse<Page<SiteReviewGetAllResponse>>> getAllSiteReviews(
            @Parameter(description = "Page number (0-based)") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @Parameter(description = "Number of records per page") @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        var response = reviewService.getAllSiteReviews(page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/site/my")
    @Operation(
            summary = "Get my site reviews",
            description = "Retrieves a list of site reviews created by the authenticated user."
    )
    public ResponseEntity<ApiResponse<List<SiteReviewGetAllResponse>>> getMySiteReviews(
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        var response = reviewService.getMySiteReviews(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/site/average-rate")
    @Operation(
            summary = "Get average site rating",
            description = "Calculates and returns the average rating of all site reviews."
    )
    public ResponseEntity<ApiResponse<Double>> getAverageRate() {
        var response = reviewService.getAverageRate();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/site/{id}")
    @Operation(
            summary = "Update a site review",
            description = "Updates an existing site review by its ID."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully toggled review status"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<ApiResponse<SiteReviewUpdateResponse>> updateSiteReview(
            @Valid @RequestBody SiteReviewUpdateRequest siteReviewUpdateRequest,
            @Parameter(description = "ID of the review to be updated") @PathVariable Long id,
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        var response = reviewService.updateSiteReview(siteReviewUpdateRequest, id, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/site/{id}")
    @Operation(
            summary = "Delete a site review",
            description = "Deletes a site review from the system by its ID."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully toggled review status"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteMySiteReview(
            @Parameter(description = "ID of the review to be deleted") @PathVariable Long id,
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        reviewService.deleteSiteReview(id, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
