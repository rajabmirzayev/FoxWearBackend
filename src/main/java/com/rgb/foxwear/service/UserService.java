package com.rgb.foxwear.service;

import com.rgb.foxwear.dto.response.user.UserGetResponse;
import com.rgb.foxwear.entity.auth.UserEntity;
import com.rgb.foxwear.exception.UserNotFoundException;
import com.rgb.foxwear.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public UserGetResponse getMe(String username) {
        log.info("Getting user by username {}", username);
        UserEntity user = findUserOrThrow(username);

        return mapper.map(user, UserGetResponse.class);
    }

    private UserEntity findUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username {}", username);
                    throw new UserNotFoundException("User not found!");
                });
    }
}
