package com.foxwear.authservice.controller;

import com.foxwear.authservice.dto.request.CreateAddressRequest;
import com.foxwear.authservice.dto.response.CreateAddressResponse;
import com.foxwear.authservice.service.AddressService;
import com.foxwear.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
@Tag(name = "Address Controller", description = "Endpoints for managing user addresses")
public class AddressController {
    private final AddressService addressService;

    @Operation(
        summary = "Create a new address",
        description = "Creates a new address for a specific user identified by their ID"
    )
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<CreateAddressResponse>> createAddress(
        @Valid @RequestBody CreateAddressRequest request,
        @PathVariable Long id
    ) {
        var response = addressService.createAddress(request, id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}