package com.rgb.foxwear.service.abstraction.auth;

import com.rgb.foxwear.dto.request.auth.CreateAddressRequest;
import com.rgb.foxwear.dto.response.auth.CreateAddressResponse;

public interface AddressService {
    CreateAddressResponse createAddress(CreateAddressRequest request, Long userId);
}
