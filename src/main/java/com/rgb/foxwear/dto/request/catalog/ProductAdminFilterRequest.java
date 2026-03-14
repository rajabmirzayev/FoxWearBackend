package com.rgb.foxwear.dto.request.catalog;

import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductAdminFilterRequest extends BaseFilterRequest {

    @Pattern(regexp = "id|title|discountPrice|createdAt|updatedAt", message = "Invalid sort field")
    String sortBy = "createdAt";

    Boolean isActive;
    Boolean isDeleted;

}
