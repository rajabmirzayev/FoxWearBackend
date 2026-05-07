package com.foxwear.orderservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalesDataDTO {

    String day;
    BigDecimal amount;

    public SalesDataDTO(Object day, Object amount) {
        if (day instanceof java.sql.Date) {
            this.day = day.toString();
        } else {
            this.day = String.valueOf(day);
        }

        this.amount = (amount instanceof BigDecimal) ? (BigDecimal) amount :
                (amount instanceof Number) ? BigDecimal.valueOf(((Number) amount).doubleValue()) :
                        BigDecimal.ZERO;
    }

}
