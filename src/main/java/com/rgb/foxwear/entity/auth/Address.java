package com.rgb.foxwear.entity.auth;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "addresses", schema = "auth")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    String title;

    @NotBlank
    @Size(min = 3, max = 30)
    @Column(nullable = false, length = 30)
    String city;

    @NotBlank
    @Size(min = 3, max = 30)
    @Column(nullable = false, length = 30)
    String region;

    @NotBlank
    @Size(min = 3, max = 30)
    @Column(nullable = false, length = 30)
    String street;

    @Size(max = 10)
    @Column(length = 10)
    String block;

    @Size(max = 10)
    @Column(length = 10)
    String floor;

    @Size(max = 10)
    @Column(name = "door_number", length = 10)
    String doorNumber;

    @Size(max = 20)
    @Column(name = "door_code", length = 20)
    String doorCode;

    @NotBlank
    @Size(max = 500)
    @Column(length = 500, nullable = false)
    String fullAddressText;

    @NotNull
    @Column(nullable = false)
    Boolean isDefault = false;

    @NotNull
    Double latitude;

    @NotNull
    Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;

}
