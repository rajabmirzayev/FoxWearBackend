package com.rgb.foxwear.entity.interaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rgb.foxwear.entity.auth.UserEntity;
import com.rgb.foxwear.entity.catalog.ColorOption;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "product_likes",
        schema = "interaction",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "product_color_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_color_id", nullable = false)
    ColorOption colorOption;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;

    @NotNull
    @CreationTimestamp
    @Column(name = "liked_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime likedAt;
}
