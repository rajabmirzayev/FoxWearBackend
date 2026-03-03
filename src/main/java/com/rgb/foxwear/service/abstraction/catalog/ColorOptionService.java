package com.rgb.foxwear.service.abstraction.catalog;

import com.rgb.foxwear.dto.request.catalog.CreateColorOptionRequest;
import com.rgb.foxwear.dto.response.catalog.CreateColorOptionResponse;

public interface ColorOptionService {
    CreateColorOptionResponse createColorOption(CreateColorOptionRequest request);
}
