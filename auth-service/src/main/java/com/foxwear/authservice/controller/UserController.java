package com.foxwear.authservice.controller;

import com.foxwear.authservice.dto.response.UserGetResponse;
import com.foxwear.authservice.service.UserService;
import com.foxwear.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "Get current user profile", description = "Retrieves the profile information of the currently authenticated user")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserGetResponse>> getMe(
            @RequestHeader(value = "X-User-Id", required = false) Long id
    ) {
        var response = userService.getUserById(id);
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

    @Operation(summary = "Get user by ID", description = "Retrieves profile information for a specific user by their unique identifier")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserGetResponse>> getUserById(
            @Parameter(description = "Unique identifier of the user", example = "1")
            @PathVariable Long id
    ) {
        var response = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
