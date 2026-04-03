package com.foxwear.orderservice.entity;

import com.foxwear.common.exception.InvalidArgumentException;
import com.foxwear.orderservice.enums.DiscountType;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    Coupon coupon;

    @Column(name = "coupon_applied", nullable = false)
    boolean couponApplied = false;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CartItem> items = new ArrayList<>();

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "total_original_price", precision = 12, scale = 2, nullable = false)
    BigDecimal totalOriginalPrice = BigDecimal.ZERO;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "total_price", precision = 12, scale = 2, nullable = false)
    BigDecimal totalPrice = BigDecimal.ZERO;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "shipping_fee", nullable = false, precision = 10, scale = 2)
    BigDecimal shippingFee;

    public void updateTotalPrice() {
        // 1. Calculate items total (Products only)
        BigDecimal itemsTotal = items.stream()
                .filter(item -> item.getProductItemId() != null)
                .map(item -> item.getActualUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        this.totalOriginalPrice = items.stream()
                .filter(item -> item.getProductItemId() != null)
                .map(item -> item.getOriginalUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        // 2. Calculate discount based on ITEMS TOTAL only
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (coupon != null && itemsTotal.compareTo(BigDecimal.ZERO) > 0) {
            if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
                BigDecimal percentage = coupon.getValue().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
                discountAmount = itemsTotal.multiply(percentage);
            } else {
                discountAmount = coupon.getValue();
            }
            this.couponApplied = true;
        } else {
            this.couponApplied = false;
        }

        // 3. Determine shipping fee based on "itemsTotal - discountAmount" (actual payment)
        BigDecimal discountedTotal = itemsTotal.subtract(discountAmount);
        if (discountedTotal.compareTo(BigDecimal.ZERO) < 0) discountedTotal = BigDecimal.ZERO;

        this.shippingFee = (discountedTotal.compareTo(BigDecimal.valueOf(70)) >= 0 || discountedTotal.compareTo(BigDecimal.ZERO) == 0)
                ? BigDecimal.ZERO : BigDecimal.valueOf(5.00);

        // 4. Final price
        this.totalPrice = discountedTotal.add(this.shippingFee).setScale(2, RoundingMode.HALF_UP);
    }
}
