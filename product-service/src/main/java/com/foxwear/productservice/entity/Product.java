package com.foxwear.productservice.entity;

import com.foxwear.common.entity.BaseAuditEntity;
import com.foxwear.common.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Check(name = "check_prices", constraints = "original_price > 0 AND discount_price < original_price")
@Check(name = "check_discount_rate", constraints = "discount_rate >= 0 AND discount_rate <= 100")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    String title;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "original_price", precision = 10, scale = 2, nullable = false)
    BigDecimal originalPrice;

    @Column(name = "discount_price", precision = 10, scale = 2)
    BigDecimal discountPrice;

    @Min(0)
    @Max(100)
    @Column(name = "discount_rate")
    Integer discountRate;

    @Column(name = "has_discount")
    boolean hasDiscount = false;

    @NotBlank
    @Size(max = 100)
    @Column(unique = true, nullable = false, length = 100)
    String slug; // ? name on link

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Gender gender;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(name = "is_active")
    boolean isActive = true;

    @Column(name = "is_deleted", nullable = false)
    boolean isDeleted = false;

    @Column(name = "liked_count", nullable = false)
    Integer likedCount = 0;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    WearCategory category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 50)
    List<ColorOption> colors = new ArrayList<>();

    @PrePersist
    @PreUpdate
    void prePersistAndUpdate() {
        int discount = this.discountRate == null ? 0 : this.discountRate;

        if (discount > 0) {
            hasDiscount = true;
        }

        if (this.discountPrice == null) {
            this.discountPrice = BigDecimal.ZERO;
        }
    }

}
