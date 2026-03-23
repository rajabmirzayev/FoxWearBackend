package com.foxwear.productservice.entity;

import com.foxwear.common.entity.BaseAuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "product_item")
@Check(name = "check_stock_integrity", constraints = "stock_remaining <= stock_quantity AND stock_remaining >= 0")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductItem extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_option_id", nullable = false)
    ColorOption colorOption;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_size_id", nullable = false)
    ProductSize productSize;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(nullable = false, unique = true, length = 50)
    String sku;

    @NotNull
    @Min(0)
    @Column(name = "stock_quantity", nullable = false)
    Integer stockQuantity;

    @NotNull
    @Min(0)
    @Column(name = "stock_remaining", nullable = false)
    Integer stockRemaining;

    @Column(name = "is_deleted")
    @Builder.Default
    boolean isDeleted = false;

    @PrePersist
    public void prePersist() {
        this.stockRemaining = this.stockQuantity;
    }

}
