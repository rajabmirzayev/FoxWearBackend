package com.foxwear.orderservice.controller.admin;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.orderservice.dto.response.DashboardSummaryResponse;
import com.foxwear.orderservice.dto.response.SalesDataDTO;
import com.foxwear.orderservice.dto.response.TopProductResponse;
import com.foxwear.orderservice.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders/dashboard")
@RequiredArgsConstructor
@Tag(name = "Admin Dashboard", description = "Admin dashboard statistics and summaries")
public class AdminDashboardController {
    private final DashboardService dashboardService;

    @Operation(summary = "Get order count summary", description = "Returns a summary of order counts categorized by status")
    @GetMapping("/order-count-summary")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getSummary() {
        var response = dashboardService.getOverview();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get sales overview", description = "Returns combined sales data for the last 7 days including revenue and order counts")
    @GetMapping("/sale-overview")
    public ResponseEntity<ApiResponse<List<SalesDataDTO>>> getSaleOverview() {
        var response = dashboardService.getCombinedSalesOverview();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get top selling products", description = "Returns a list of top selling products based on order quantity")
    @GetMapping("/top-products")
    public ResponseEntity<ApiResponse<List<TopProductResponse>>> getTopProducts(
            @RequestParam(defaultValue = "5") int limit
    ) {
        var response = dashboardService.getTopProducts(limit);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
