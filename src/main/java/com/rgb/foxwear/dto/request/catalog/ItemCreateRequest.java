package com.rgb.foxwear.dto.request.catalog;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemCreateRequest {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    Long sizeId;

    @NotNull
    @Min(0)
    Integer stockQuantity;

}
