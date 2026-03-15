package com.rgb.foxwear.controller;

import com.rgb.foxwear.dto.ApiResponse;
import com.rgb.foxwear.dto.request.auth.LoginRequest;
import com.rgb.foxwear.dto.request.auth.RegisterRequest;
import com.rgb.foxwear.dto.response.auth.AuthResponse;
import com.rgb.foxwear.service.auth.AuthService;
import com.rgb.foxwear.service.auth.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<@NonNull ApiResponse<Void>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        authService.register(request);
        return ResponseEntity.ok(ApiResponse.success(null, "User registered successfully, please check your email"));
    }

    @PostMapping("/login")
    public ResponseEntity<@NonNull ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<@NonNull ApiResponse<AuthResponse>> refresh(
            @RequestParam String refreshToken
    ) {
        var response = authService.refresh(refreshToken);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<@NonNull ApiResponse<String>> logout(@RequestParam String refreshToken) {
        refreshTokenService.revokeRefreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.success(null, "Successfully logged out"));
    }

}
