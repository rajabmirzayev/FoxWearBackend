package com.foxwear.authservice.entity;

import com.foxwear.common.entity.BaseAuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "saved_cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;

    @NotBlank
    @Size(max = 50)
    @Column(name = "card_holder_name", nullable = false, length = 50)
    String cardHolderName;

    @NotBlank
    @Pattern(regexp = "\\*\\*\\*\\* \\d{4}$")
    @Column(name = "masked_number", nullable = false, length = 25)
    String maskedNumber;

    @NotBlank
    @Column(name = "payment_provider_token", nullable = false)
    String providerToken;

    @NotBlank
    @Size(min = 5, max = 5)
    @Column(name = "expiry_date", nullable = false, length = 5)
    String expiryDate;

    @NotBlank
    @Size(max = 20)
    @Column(name = "card_type", length = 20)
    String cardType;

    @Column(name = "is_default")
    boolean isDefault = false;

    @Column(name = "is_expired")
    boolean isExpired = false;

}