package com.foxwear.interactionservice.mapper;

import com.foxwear.interactionservice.dto.request.MessageCreateRequest;
import com.foxwear.interactionservice.dto.response.MessageCreateResponse;
import com.foxwear.interactionservice.entity.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    Message toEntity(MessageCreateRequest request);

    MessageCreateResponse toCreateResponse(Message savedMessage);

}
