package com.rgb.foxwear.entity.ordering;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rgb.foxwear.entity.auth.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "courier_locations",
        schema = "ordering",
        indexes = {
                @Index(name = "idx_courier_id", columnList = "courier_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourierLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id", unique = true, nullable = false)
    UserEntity courier;

    @Column(name = "current_latitude")
    Double currentLatitude;

    @Column(name = "current_longitude")
    Double currentLongitude;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @UpdateTimestamp
    @Column(name = "last_updated_at")
    LocalDateTime lastUpdatedAt;

    @Column(name = "is_active")
    boolean isActive = true;
}