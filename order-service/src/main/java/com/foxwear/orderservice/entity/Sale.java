package com.foxwear.orderservice.entity;

import com.foxwear.common.entity.BaseAuditEntity;
import com.foxwear.orderservice.enums.SalePaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Sale extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "receipt_number", unique = true, nullable = false)
    String receiptNumber;

    @Column(name = "cashier_id", nullable = false)
    Long cashierId;

    @Column(name = "total_amount", nullable = false)
    BigDecimal totalAmount;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    SalePaymentMethod paymentMethod;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<SaleItem> items = new ArrayList<>();

}
