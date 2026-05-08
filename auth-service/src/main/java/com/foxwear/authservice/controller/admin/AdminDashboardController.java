package com.foxwear.authservice.controller.admin;

import com.foxwear.authservice.dto.response.UserStatsResponse;
import com.foxwear.authservice.service.DashboardService;
import com.foxwear.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users/dashboard")
@RequiredArgsConstructor
@Tag(name = "Admin Dashboard", description = "Endpoints for administrator dashboard statistics")
public class AdminDashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/new-users")
    @Operation(summary = "Get user dashboard statistics", description = "Retrieves a summary of user-related statistics for the admin dashboard")
    public ResponseEntity<ApiResponse<UserStatsResponse>> getSummary() {
        var response = dashboardService.getUserDashboardStats();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
