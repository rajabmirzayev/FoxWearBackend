package com.foxwear.interactionservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "site_reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SiteReview extends AbstractReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

}