package com.foxwear.orderservice.controller.admin;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.orderservice.dto.response.SaleGetAllResponse;
import com.foxwear.orderservice.dto.response.SaleGetResponse;
import com.foxwear.orderservice.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/sellers")
@RequiredArgsConstructor
@Tag(name = "Admin Seller Controller", description = "Management APIs for Sales/Sellers by Admin")
public class AdminSellerController {
    private final SaleService saleService;

    @Operation(summary = "Get all sales", description = "Retrieves a paginated list of all sales records")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<SaleGetAllResponse>>> getAllSales(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        var response = saleService.getAll(page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get sale details by ID", description = "Retrieves detailed information about a specific sale by its unique identifier")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SaleGetResponse>> getSaleDetailsById(
            @PathVariable Long id
    ) {
        var response = saleService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
