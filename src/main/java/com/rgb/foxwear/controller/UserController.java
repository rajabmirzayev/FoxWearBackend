package com.rgb.foxwear.controller;

import com.rgb.foxwear.dto.ApiResponse;
import com.rgb.foxwear.dto.request.auth.CreateUserRequest;
import com.rgb.foxwear.dto.response.auth.CreateUserResponse;
import com.rgb.foxwear.service.abstraction.auth.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<@NonNull ApiResponse<CreateUserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest createUserRequest
    ) {
        var response = userService.createUser(createUserRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
