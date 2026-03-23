package com.foxwear.dynamicdataservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BannerRequest {

    String imageUrl;
    String mobileImageUrl;

    @Size(max = 100, message = "Title must not exceed 100 characters")
    @NotNull(message = "Title is required")
    String title;

    @Size(max = 255, message = "Subtitle must not exceed 255 characters")
    String subtitle;

    @Size(max = 50, message = "Button text must not exceed 50 characters")
    String buttonText;
    String buttonLink;

    @NotNull(message = "Sort order is required")
    Integer sortOrder = 0;

    @NotBlank(message = "Placement is required")
    @Size(max = 50, message = "Placement must not exceed 50 characters")
    String placement;

    boolean active = true;

}
