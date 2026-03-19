package com.rgb.foxwear.dto.response.dynamic;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BannerResponse {

    Long id;
    String imageUrl;
    String mobileImageUrl;
    String title;
    String subtitle;
    String buttonText;
    String buttonLink;
    Integer sortOrder = 0;
    String placement;
    boolean active = true;

}
