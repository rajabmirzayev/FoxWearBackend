package com.foxwear.interactionservice.controller;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.interactionservice.dto.request.ProductReviewCreateRequest;
import com.foxwear.interactionservice.dto.request.ProductReviewUpdateRequest;
import com.foxwear.interactionservice.dto.request.SiteReviewCreateRequest;
import com.foxwear.interactionservice.dto.request.SiteReviewUpdateRequest;
import com.foxwear.interactionservice.dto.response.*;
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

    @PostMapping("/product/{id}")
    @Operation(
            summary = "Create a product review",
            description = "Allows a user to submit a new review for a specific product."
    )
    public ResponseEntity<ApiResponse<ProductReviewCreateResponse>> createProductReview(
            @Valid @RequestBody ProductReviewCreateRequest productReviewCreateRequest,
            @Parameter(description = "ID of the product being reviewed", required = true) @PathVariable Long id,
            @Parameter(description = "ID of the user creating the review", required = true) @RequestHeader(value = "X-User-Id") Long userId
    ) {
        var response = reviewService.createProductReview(productReviewCreateRequest, id, userId);
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

    @GetMapping("/product/{id}")
    @Operation(
            summary = "Get all product reviews",
            description = "Retrieves a paginated list of all product reviews across all products."
    )
    public ResponseEntity<ApiResponse<Page<ProductReviewGetAllResponse>>> getAllProductReviews(
            @Parameter(description = "Page number (0-based)") @RequestParam(value = "page", defaultValue = "0") Integer page,
            @Parameter(description = "Number of records per page") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @PathVariable Long id
    ) {
        var response = reviewService.getAllProductReviews(page, size, id);
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

    @GetMapping("/product/my")
    @Operation(
            summary = "Get my product reviews",
            description = "Retrieves a list of product reviews created by the authenticated user."
    )
    public ResponseEntity<ApiResponse<List<ProductReviewGetAllResponse>>> getMyProductReviews(
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        var response = reviewService.getMyProductReviews(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/site/average-rate")
    @Operation(
            summary = "Get average site rating",
            description = "Calculates and returns the average rating of all site reviews."
    )
    public ResponseEntity<ApiResponse<Double>> getAverageRate() {
        var response = reviewService.getAverageSiteRate();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/product/average-rate/{id}")
    @Operation(
            summary = "Get average product rating",
            description = "Calculates and returns the average rating for a specific product."
    )
    public ResponseEntity<ApiResponse<Double>> getAverageProductReviewRate(
            @Parameter(description = "ID of the product") @PathVariable Long id
    ) {
        var response = reviewService.getAverageProductRate(id);
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

    @PutMapping("/product/{id}")
    @Operation(
            summary = "Update a product review",
            description = "Updates an existing product review by its ID."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully updated product review"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<ApiResponse<ProductReviewUpdateResponse>> updateProductReview(
            @Valid @RequestBody ProductReviewUpdateRequest productReviewUpdateRequest,
            @Parameter(description = "ID of the review to be updated") @PathVariable Long id,
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        var response = reviewService.updateProductReview(productReviewUpdateRequest, id, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/site/{id}")
    @Operation(
            summary = "Delete a site review",
            description = "Deletes a site review from the system by its ID."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully deleted site review"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteMySiteReview(
            @Parameter(description = "ID of the review to be deleted") @PathVariable Long id,
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        reviewService.deleteSiteReview(id, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/product/{id}")
    @Operation(
            summary = "Delete a product review",
            description = "Deletes a product review from the system by its ID."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully deleted product review"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Review not found")
    })
    public  ResponseEntity<ApiResponse<Void>> deleteProductReview(
            @Parameter(description = "ID of the review to be deleted") @PathVariable Long id,
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        reviewService.deleteProductReview(id, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
