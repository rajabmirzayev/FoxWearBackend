package com.foxwear.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "sale_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    Sale sale;

    @Column(name = "product_item_id", nullable = false)
    Long productItemId;

    @Column(nullable = false)
    Integer quantity;

    @Column(name = "unit_price", nullable = false)
    BigDecimal unitPrice;

    @Column(name = "total_price", nullable = false)
    BigDecimal totalPrice;

}
