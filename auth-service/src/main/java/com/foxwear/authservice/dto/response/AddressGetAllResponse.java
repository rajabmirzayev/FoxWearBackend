package com.foxwear.authservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressGetAllResponse {

    Long id;
    String title;
    String city;
    String region;
    String street;
    String block;
    String floor;
    String doorNumber;
    String doorCode;
    String fullAddressText;
    Boolean isDefault;

}
