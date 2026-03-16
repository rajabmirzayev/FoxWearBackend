package com.rgb.foxwear.entity.dynamic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "banners", schema = "dynamic")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "image_url")
    String imageUrl;

    @Column(name = "mobile_image_url")
    String mobileImageUrl;

    @Size(max = 100)
    @Column(length = 100)
    String title;

    @Size(max = 255)
    String subtitle;

    @Size(max = 50)
    @Column(name = "button_text", length = 50)
    String buttonText;

    @Column(name = "button_link")
    String buttonLink;

    @NotNull
    @Column(name = "sort_order", nullable = false)
    Integer sortOrder = 0;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    String placement;

    @Column(name = "is_active", nullable = false)
    boolean active = true;

}
