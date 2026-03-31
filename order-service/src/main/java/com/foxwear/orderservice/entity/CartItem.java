package com.foxwear.orderservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "cart_items")
@Check(name = "check_cart_item_prices", constraints = "original_unit_price >= 0 AND actual_unit_price >= 0 AND sub_total >= 0")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    Cart cart;

    @NotNull
    @Column(name = "product_item_id", nullable = false)
    Long productItemId;

    @NotNull
    @Column(name = "product_name", nullable = false)
    String productName;

    @NotNull
    @Column(name = "color_name", nullable = false)
    String colorName;

    @NotNull
    @Column(name = "image_url", nullable = false)
    String imageUrl;

    @NotNull
    @Column(name = "size_value", nullable = false)
    String sizeValue;

    @NotNull
    @Column(nullable = false)
    String slug;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    Integer quantity;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "original_unit_price", nullable = false, precision = 12, scale = 2)
    BigDecimal originalUnitPrice;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "actual_unit_price", nullable = false, precision = 12, scale = 2)
    BigDecimal actualUnitPrice;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "sub_total", nullable = false, precision = 12, scale = 2)
    BigDecimal subTotal;

    public void updateSubtotal() {
        this.actualUnitPrice = (this.actualUnitPrice == null) ? BigDecimal.ZERO : this.actualUnitPrice;

        int qty = (this.quantity == null) ? 0 : this.quantity;

        this.subTotal = this.actualUnitPrice.multiply(BigDecimal.valueOf(qty))
                .setScale(2, RoundingMode.HALF_UP);
    }

}