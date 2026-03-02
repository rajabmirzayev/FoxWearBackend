package com.rgb.foxwear.entity.ordering;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rgb.foxwear.entity.BaseAuditEntity;
import com.rgb.foxwear.entity.auth.UserEntity;
import com.rgb.foxwear.entity.catalog.Coupon;
import com.rgb.foxwear.enums.OrderStatus;
import com.rgb.foxwear.enums.PaymentMethod;
import com.rgb.foxwear.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "orders",
        schema = "ordering",
        check = {
                @CheckConstraint(name = "check_total_prices", constraint = "total_original_price >= 0 AND total_discount_price >= 0")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Size(max = 20)
    @Column(name = "order_number", nullable = false, unique = true, length = 20)
    String orderNumber;

    @NotNull
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    UserEntity user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    OrderStatus status;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "total_original_price", precision = 12, scale = 2, nullable = false)
    BigDecimal totalOriginalPrice;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "total_discount_price", precision = 12, scale = 2, nullable = false)
    BigDecimal totalDiscountPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    Coupon coupon;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "shipping_fee", nullable = false, precision = 10, scale = 2)
    BigDecimal shippingFee;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 25)
    PaymentStatus paymentStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 25)
    PaymentMethod paymentMethod;

    @NotBlank
    @Size(max = 700)
    @Column(name = "address_snapshot", nullable = false, length = 700)
    String addressSnapshot;

    @Column(name = "latitude_snapshot")
    Double latitudeSnapshot;

    @Column(name = "longitude_snapshot")
    Double longitudeSnapshot;

    @Size(max = 500)
    @Column(name = "order_note", length = 500)
    String orderNote;

    @Size(max = 30)
    @Column(name = "tracking_number", unique = true, length = 30)
    String trackingNumber;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "estimated_delivery_date")
    LocalDateTime estimatedDeliveryDate;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "delivered_at")
    LocalDateTime deliveredAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> items = new ArrayList<>();

}
