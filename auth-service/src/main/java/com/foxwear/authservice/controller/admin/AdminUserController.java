package com.foxwear.authservice.controller.admin;

import com.foxwear.authservice.dto.request.UserFilterRequest;
import com.foxwear.authservice.dto.response.UserGetAllResponse;
import com.foxwear.authservice.dto.response.UserGetResponse;
import com.foxwear.authservice.service.UserService;
import com.foxwear.common.dto.ApiResponse;
import com.foxwear.common.enums.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "Get all users", description = "Retrieves a filtered list of all users based on the provided criteria")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserGetAllResponse>>> getAllUsers(
            @Valid @ModelAttribute UserFilterRequest userFilterRequest,
            @RequestHeader(value = "X-User-Id", required = false) Long userId
    ) {
        var response = userService.getAllUsers(userFilterRequest, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all user statuses", description = "Retrieves a list of all possible user account statuses")
    @GetMapping("/statuses")
    public ResponseEntity<ApiResponse<List<String>>> getStatuses() {
        var response = userService.getAllStatuses();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all genders", description = "Retrieves a list of all supported gender options")
    @GetMapping("/genders")
    public ResponseEntity<ApiResponse<List<String>>> getGenders() {
        var response = userService.getAllGenders();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all roles", description = "Retrieves a list of all available user roles in the system")
    @GetMapping("/roles")
    public ResponseEntity<ApiResponse<List<String>>> getRoles() {
        var response = userService.getAllRoles();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Change user role", description = "Updates the role of a specific user")
    @PatchMapping("/{id}/role")
    public ResponseEntity<ApiResponse<Void>> changeRole(
            @Parameter(description = "Unique identifier of the user", example = "1")
            @PathVariable Long id,
            @Parameter(description = "New role to assign to the user", example = "ADMIN")
            @RequestParam Role role
    ) {
        userService.changeUserRole(id, role);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
