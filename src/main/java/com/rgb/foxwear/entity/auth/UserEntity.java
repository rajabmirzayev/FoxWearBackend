package com.rgb.foxwear.entity.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rgb.foxwear.entity.BaseAuditEntity;
import com.rgb.foxwear.entity.interaction.ProductLike;
import com.rgb.foxwear.entity.ordering.Cart;
import com.rgb.foxwear.enums.Gender;
import com.rgb.foxwear.enums.Role;
import com.rgb.foxwear.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users", schema = "auth")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity extends BaseAuditEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Size(min = 3, max = 30)
    @Column(name = "first_name", nullable = false, length = 30)
    String firstName;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(name = "last_name", nullable = false, length = 50)
    String lastName;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(unique = true, nullable = false, length = 50)
    String username;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    String email;

    @NotBlank
    @Size(min = 12, max = 17)
    @Column(name = "phone_number", unique = true, nullable = false, length = 17)
    String phoneNumber;

    @NotBlank
    @Size(min = 8, max = 255)
    @Column(nullable = false)
    String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Gender gender;

    @Column(name = "birth_date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate birthDate;

    @Size(max = 10000)
    @Column(name = "profile_picture", length = 10000)
    String profilePicture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    UserStatus status = UserStatus.PENDING;

    @Column(name = "is_email_verified")
    boolean isEmailVerified = false;

    @Column(name = "is_phone_number_verified")
    boolean isPhoneNumberVerified = false;

    @Column(name = "two_factor_enabled")
    boolean twoFactorEnabled = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Address> addresses = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    Cart cart;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductLike> likedProducts = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<Card> savedCards = new ArrayList<>();

    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != UserStatus.BANNED;
    }

    @Override
    public boolean isEnabled() {
        return status == UserStatus.ACTIVE;
    }
}