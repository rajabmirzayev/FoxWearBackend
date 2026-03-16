package com.rgb.foxwear.controller;

import com.rgb.foxwear.dto.ApiResponse;
import com.rgb.foxwear.dto.response.interaction.ReviewGetAllResponse;
import com.rgb.foxwear.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Review Controller", description = "Endpoints for managing and retrieving site reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/site/first-10")
    @Operation(
            summary = "Get first 10 site reviews",
            description = "Retrieves the latest 10 active site reviews along with user information."
    )
    public ResponseEntity<@NonNull ApiResponse<List<ReviewGetAllResponse>>> getFirst10Reviews() {
        var response = reviewService.getFirst10Reviews();
        return  ResponseEntity.ok(ApiResponse.success(response));
    }

}
