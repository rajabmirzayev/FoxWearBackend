package com.foxwear.common.event;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeEvent {

    Long productId;
    boolean isLiked;

}
