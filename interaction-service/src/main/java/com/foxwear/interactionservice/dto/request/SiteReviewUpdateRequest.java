package com.foxwear.interactionservice.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SiteReviewUpdateRequest {

    @NotNull
    @Min(1)
    @Max(5)
    @Column(nullable = false)
    Integer rate;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    String description;

}
