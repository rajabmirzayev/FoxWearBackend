package com.foxwear.orderservice.dto.request;

import com.foxwear.orderservice.enums.OrderStatus;
import com.foxwear.orderservice.enums.PaymentMethod;
import com.foxwear.orderservice.enums.PaymentStatus;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderFilterRequest {

    List<OrderStatus> orderStatuses;
    List<PaymentStatus> paymentStatuses;
    List<PaymentMethod> paymentMethods;

    @Pattern(regexp = "totalDiscountPrice|createdAt|updatedAt", message = "Invalid sort field")
    String sortBy = "updatedAt";

    String searchKeyword;
    Integer page;
    Integer size;

    Sort.Direction direction;

}
