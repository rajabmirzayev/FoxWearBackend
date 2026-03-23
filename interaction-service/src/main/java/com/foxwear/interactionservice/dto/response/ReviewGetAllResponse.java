package com.foxwear.interactionservice.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewGetAllResponse {

    Integer rate;
    String description;
    Long userId;
    boolean isActive = true;

}
