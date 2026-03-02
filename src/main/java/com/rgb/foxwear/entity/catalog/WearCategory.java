package com.rgb.foxwear.entity.catalog;

import com.rgb.foxwear.entity.BaseAuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories", schema = "catalog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WearCategory extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Size(max = 30)
    @Column(nullable = false, unique = true, length = 30)
    String name;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    String subtitle;

    @Size(max = 255)
    @Column(unique = true)
    String link;

    @NotBlank
    @Size(max = 10000)
    @Column(name = "main_image", length = 10000)
    String mainImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    WearCategory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    List<WearCategory> subCategories = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    List<Product> products = new ArrayList<>();

}