package com.rgb.foxwear.entity.interaction;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "site_reviews", schema = "interaction")
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