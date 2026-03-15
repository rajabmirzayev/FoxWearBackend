package com.rgb.foxwear.controller;

import com.rgb.foxwear.dto.ApiResponse;
import com.rgb.foxwear.dto.request.auth.CreateAddressRequest;
import com.rgb.foxwear.dto.response.auth.CreateAddressResponse;
import com.rgb.foxwear.service.AddressService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping("/{id}")
    public ResponseEntity<@NonNull ApiResponse<CreateAddressResponse>> createAddress(
        @Valid @RequestBody CreateAddressRequest request,
        @PathVariable Long id
    ) {
        var response = addressService.createAddress(request, id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}