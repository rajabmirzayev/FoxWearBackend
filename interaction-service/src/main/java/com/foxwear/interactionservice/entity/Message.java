package com.foxwear.interactionservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "messages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    Long userId;

    @NotBlank
    @Column(nullable = false)
    String email;

    @NotBlank
    @Column(name = "full_name", nullable = false)
    String fullName;

    @NotBlank
    @Column(nullable = false)
    String subject;

    @NotBlank
    @Column(nullable = false)
    String message;

    @NotNull
    @Column(name = "is_answered", nullable = false)
    boolean isAnswered = false;

}
