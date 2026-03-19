package com.rgb.foxwear.dto.response.interaction;

import com.rgb.foxwear.dto.response.auth.UserGetReviewResponse;
import com.rgb.foxwear.entity.auth.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewGetAllResponse {

    Integer rate;
    String description;
    UserGetReviewResponse user;
    boolean isActive = true;

}
