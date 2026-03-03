package com.rgb.foxwear.dto.response.catalog;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCategoryResponse {

    Long id;
    String name;
    String subtitle;
    String link;
    String mainImage;
    CreateCategoryResponse parent;

}
