package com.foxwear.productservice.client;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.productservice.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Set;

@FeignClient(name = "INTERACTION-SERVICE", configuration = FeignClientConfig.class)
public interface InteractionClient {

    @GetMapping("/api/v1/likes/my-liked-ids")
    ApiResponse<Set<Long>> getMyLikedIds(
            @RequestHeader(value = "X-User-Id", required = false) Long id
    );

}
