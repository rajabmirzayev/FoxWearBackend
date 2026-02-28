package com.rgb.foxwear.entity.ordering;

import com.rgb.foxwear.entity.catalog.ProductItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(
        name = "cart_items",
        schema = "ordering",
        check = {
                @CheckConstraint(name = "check_cart_item_prices", constraint = "original_unit_price >= 0 AND actual_unit_price >= 0 AND sub_total >= 0")
        }
)
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_item_id", nullable = false)
    ProductItem productItem;

    @NotNull
    @Min(1)
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

    @PrePersist
    @PreUpdate
    public void calculateSubTotal() {
        this.actualUnitPrice = (this.actualUnitPrice == null) ? BigDecimal.ZERO : this.actualUnitPrice;

        int qty = (this.quantity == null) ? 0 : this.quantity;

        this.subTotal = this.actualUnitPrice.multiply(BigDecimal.valueOf(qty))
                .setScale(2, RoundingMode.HALF_UP);
    }

}