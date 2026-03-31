package com.foxwear.orderservice.client;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.common.dto.response.ProductResponse;
import com.foxwear.orderservice.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "PRODUCT-SERVICE", configuration = FeignClientConfig.class)
public interface ProductClient {

    @GetMapping("/api/v1/products/{id}/price")
    ApiResponse<BigDecimal> getProductPrice(
            @PathVariable Long id
    );

    @GetMapping("/api/v1/products/basic/{itemId}")
    ApiResponse<ProductResponse> getProductWithItemId(
            @PathVariable Long itemId
    );

}
