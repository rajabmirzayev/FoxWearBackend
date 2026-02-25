package com.rgb.foxwear.entity.catalog;

import com.rgb.foxwear.entity.BaseAuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "product_item", schema = "catalog")
@Getter
@Setter
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
    @ManyToOne(fetch = FetchType.LAZY)
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
    boolean isDeleted = false;

}
