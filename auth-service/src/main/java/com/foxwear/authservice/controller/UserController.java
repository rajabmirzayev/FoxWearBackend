package com.foxwear.authservice.controller;

import com.foxwear.authservice.dto.request.UserUpdateRequest;
import com.foxwear.authservice.dto.response.UserGetPublicResponse;
import com.foxwear.authservice.dto.response.UserGetResponse;
import com.foxwear.authservice.dto.response.UserUpdateResponse;
import com.foxwear.authservice.service.UserService;
import com.foxwear.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Endpoints for managing user profiles")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Update user profile", description = "Updates the profile information for the authenticated user")
    @PutMapping
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateUser(
            @Valid @RequestBody UserUpdateRequest userUpdateRequest,
            @Parameter(description = "ID of the authenticated user", hidden = true)
            @RequestHeader(value = "X-User-Id") Long id
    ) {
        var response = userService.updateUser(userUpdateRequest, id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get current user profile", description = "Retrieves the profile information of the currently authenticated user")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserGetResponse>> getMe(
            @Parameter(description = "ID of the authenticated user", hidden = true)
            @RequestHeader(value = "X-User-Id", required = false) Long id
    ) {
        var response = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get public user profile", description = "Retrieves public profile information of a user by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserGetPublicResponse>> getUserById(
            @Parameter(description = "Unique identifier of the user", example = "1")
            @PathVariable Long id
    ) {
        var response = userService.getUserForPublicById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Check if username exists", description = "Checks if a specific username is already taken in the system")
    @GetMapping("username-exists/{username}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<Boolean>> existsUsername(
            @Parameter(description = "Username to check for availability", example = "john_doe")
            @PathVariable String username
    ) {
        var response = userService.existsUsername(username);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
