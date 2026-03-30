package com.foxwear.authservice.controller;

import com.foxwear.authservice.dto.request.AddressCreateRequest;
import com.foxwear.authservice.dto.request.AddressUpdateRequest;
import com.foxwear.authservice.dto.response.AddressCreateResponse;
import com.foxwear.authservice.dto.response.AddressGetAllResponse;
import com.foxwear.authservice.dto.response.AddressGetResponse;
import com.foxwear.authservice.dto.response.AddressUpdateResponse;
import com.foxwear.authservice.service.AddressService;
import com.foxwear.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
@Tag(name = "Address Controller", description = "Endpoints for managing user addresses")
public class AddressController {
    private final AddressService addressService;

    @Operation(
            summary = "Create a new address",
            description = "Creates a new address for a specific user identified by their ID"
    )
    @PostMapping
    public ResponseEntity<ApiResponse<AddressCreateResponse>> createAddress(
            @Valid @RequestBody AddressCreateRequest request,
            @RequestHeader("X-User-Id") Long id
    ) {
        var response = addressService.createAddress(request, id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Get address by ID",
            description = "Retrieves a specific address by its ID for the authenticated user"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressGetResponse>> getAddressById(
            @Parameter(description = "ID of the address to retrieve") @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId
    ) {
        var response = addressService.getAddressById(id, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Get all addresses",
            description = "Retrieves all addresses associated with the authenticated user"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressGetAllResponse>>> getAllAddresses(
            @RequestHeader("X-User-Id") Long userId
    ) {
        var response = addressService.getAllAddresses(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Update an existing address",
            description = "Updates the address details for a specific address ID belonging to the authenticated user"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressUpdateResponse>> updateAddress(
            @Valid @RequestBody AddressUpdateRequest request,
            @Parameter(description = "ID of the address to be updated") @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId
    ) {
        var response = addressService.updateAddress(request, id, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Delete an address",
            description = "Deletes a specific address belonging to the authenticated user"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @Parameter(description = "ID of the address to be deleted") @PathVariable Long id,
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        addressService.deleteAddress(id, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}