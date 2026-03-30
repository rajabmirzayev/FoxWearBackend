package com.foxwear.orderservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    Long userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CartItem> items = new ArrayList<>();

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "total_price", precision = 12, scale = 2, nullable = false)
    BigDecimal totalPrice = BigDecimal.ZERO;

    @SuppressWarnings("unused") // TODO delete when use
    public void updateTotalPrice() {
        this.totalPrice = items.stream()
                .filter(item -> item.getProductItemId() != null)
                .map(item -> item.getSubTotal() != null ? item.getSubTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

}
