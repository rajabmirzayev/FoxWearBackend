package com.foxwear.orderservice.dto.request;

import com.foxwear.orderservice.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreateRequest {

    @NotNull
    PaymentMethod paymentMethod;

    @NotBlank
    @Size(max = 700)
    String addressSnapshot;

    @Size(max = 500)
    String orderNote;

    Double latitude;
    Double longitude;
    Long couponId;

}
