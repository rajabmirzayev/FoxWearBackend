package com.foxwear.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateAddressRequest {

    @NotBlank(message = "Address title is required")
    @Size(max = 50, message = "Address title must be at most 50 characters long")
    String title;

    @NotBlank(message = "City is required")
    @Size(min = 3, max = 30, message = "City must be between 3 and 30 characters long")
    String city;

    @NotBlank(message = "Region is required")
    @Size(min = 3, max = 30, message = "Region must be between 3 and 30 characters long")
    String region;

    @NotBlank(message = "Street is required")
    @Size(min = 3, max = 30, message = "Street must be between 3 and 30 characters long")
    String street;

    @Size(max = 10, message = "Block must be at most 10 characters long")
    String block;

    @Size(max = 10, message = "Floor must be at most 10 characters long")
    String floor;

    @Size(max = 10, message = "Door number must be at most 10 characters long")
    String doorNumber;

    @Size(max = 20, message = "Door code must be at most 20 characters long")
    String doorCode;

    @NotBlank(message = "Full address is required")
    @Size(max = 500, message = "Full address text must be at most 500 characters long")
    String fullAddressText;

    @NotNull(message = "Latitude is required")
    Double latitude;

    @NotNull(message = "Longitude is required")
    Double longitude;

    @NotNull(message = "Default address status must be specified")
    Boolean isDefault;

}