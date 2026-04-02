package com.foxwear.orderservice.dto.request;

import com.foxwear.orderservice.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @NotNull
    @Pattern(
            regexp = "^(\\+994\\s(50|51|55|70|77|99|10)\\s\\d{3}\\s\\d{2}\\s\\d{2})?$",
            message = "Phone number must this format: +994 12 345 67 89)"
    )
    String phoneNumber;

}
