package com.foxwear.productservice.dto.request;

import com.foxwear.common.enums.Gender;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseFilterRequest implements FilterDTO {

    @Min(value = 0, message = "Page must be non-negative")
    Integer page = 0;

    @Min(value = 1, message = "Size must be at least 1")
    @Max(value = 100, message = "Size must not exceed 100")
    Integer size = 10;

    Sort.Direction direction = Sort.Direction.DESC;
    List<Gender> gender;
    List<Long> categoryId;

    @Size(max = 100, message = "Keyword too long")
    String keyword;

    List<String> color;
    List<String> productSize;

    BigInteger minPrice;
    BigInteger maxPrice;

}
