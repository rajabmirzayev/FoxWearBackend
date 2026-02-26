package com.rgb.foxwear.entity.catalog;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_color_options", schema = "catalog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColorOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Size(max = 30)
    @Column(name = "color_name", nullable = false, length = 30)
    String colorName;

    @NotBlank
    @Size(max = 30)
    @Column(name = "color_code", nullable = false, length = 30)
    String colorCode;

    @OneToMany(mappedBy = "colorOption", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ColorOptionImage> images = new ArrayList<>();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @OneToMany(mappedBy = "colorOption", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductItem> items = new ArrayList<>();

}