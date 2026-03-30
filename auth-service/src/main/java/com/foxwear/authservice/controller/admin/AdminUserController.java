package com.foxwear.authservice.controller.admin;

import com.foxwear.authservice.dto.response.UserGetResponse;
import com.foxwear.authservice.service.UserService;
import com.foxwear.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @Operation(summary = "Get user by ID", description = "Retrieves profile information for a specific user by their unique identifier")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserGetResponse>> getUserById(
            @Parameter(description = "Unique identifier of the user", example = "1")
            @PathVariable Long id
    ) {
        var response = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
