package com.foxwear.interactionservice.controller;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.interactionservice.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @Operation(summary = "Like or unlike a product", description = "Toggles the like status of a product for the authenticated user. Returns 'Liked' or 'Unliked' accordingly.")
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> likeProduct(
            @Parameter(name = "id", description = "ID of the product to like or unlike", required = true, example = "1")
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long userId
    ) {
        boolean isLiked = likeService.likeProduct(id, userId);

        return ResponseEntity.ok(ApiResponse.success(null, isLiked ? "Liked" : "Unliked"));
    }

    @Operation(summary = "Get liked product IDs", description = "Retrieves a set of product IDs that the authenticated user has liked.")
    @GetMapping("/my-liked-ids")
    public ResponseEntity<ApiResponse<Set<Long>>> getMyLikedIds(
            @Parameter(name = "X-User-Id", description = "ID of the authenticated user", hidden = true)
            @RequestHeader(value = "X-User-Id", required = false) Long id
    ) {
        Set<Long> likedIds = likeService.getMyLikedIds(id);

        return ResponseEntity.ok(ApiResponse.success(likedIds));
    }
}
