package com.foxwear.orderservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @NotNull
    @Column(name = "product_item_id", nullable = false)
    Long productItemId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "product_title_snapshot", nullable = false, length = 100)
    String productTitleSnapshot;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    Integer quantity;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "price_at_purchase", precision = 10, scale = 2, nullable = false)
    BigDecimal priceAtPurchase;

    @Column(name = "discount_at_purchase", precision = 10, scale = 2)
    BigDecimal discountAtPurchase;

    @NotNull
    @Column(name = "sub_total", nullable = false, precision = 12, scale = 2)
    BigDecimal subTotal;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(nullable = false, length = 50)
    String skuSnapshot;

    @NotNull
    @Column(name = "is_reviewed", nullable = false)
    boolean isReviewed = false;

    @PrePersist
    @PreUpdate
    void prePersistAndUpdate() {
        BigDecimal discount = (discountAtPurchase != null) ? discountAtPurchase : BigDecimal.ZERO;

        this.subTotal = this.priceAtPurchase
                .subtract(discount)
                .multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP);
    }

}
