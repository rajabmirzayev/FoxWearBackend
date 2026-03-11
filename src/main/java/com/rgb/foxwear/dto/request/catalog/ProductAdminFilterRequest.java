package com.rgb.foxwear.dto.request.catalog;

import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductAdminFilterRequest extends BaseFilterRequest {

    @Pattern(regexp = "id|title|originalPrice|createdAt", message = "Invalid sort field")
    String sortBy = "createdAt";

    Boolean isActive;
    Boolean isDeleted;

}
