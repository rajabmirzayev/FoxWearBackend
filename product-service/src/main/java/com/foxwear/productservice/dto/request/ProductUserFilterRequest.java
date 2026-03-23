package com.foxwear.productservice.dto.request;

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
public class ProductUserFilterRequest extends BaseFilterRequest {

    @Pattern(regexp = "discountPrice|createdAt", message = "Invalid sort field")
    String sortBy = "updatedAt";

}
