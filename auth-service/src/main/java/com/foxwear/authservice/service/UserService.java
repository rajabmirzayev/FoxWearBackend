package com.foxwear.authservice.service;

import com.foxwear.authservice.dto.request.UserUpdateRequest;
import com.foxwear.authservice.dto.response.UserGetPublicResponse;
import com.foxwear.authservice.dto.response.UserGetResponse;
import com.foxwear.authservice.dto.response.UserUpdateResponse;
import com.foxwear.authservice.entity.UserEntity;
import com.foxwear.authservice.exception.UserNotFoundException;
import com.foxwear.authservice.mapper.UserMapper;
import com.foxwear.authservice.repository.UserRepository;
import com.foxwear.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserUpdateResponse updateUser(UserUpdateRequest request, Long id) {
        if (id == null) {
            throw new UnauthorizedException("Unauthorized user!");
        }

        UserEntity user = findUserOrThrow(id);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setGender(request.getGender());
        user.setBirthDate(request.getBirthDate());
        user.setProfilePicture(request.getProfilePicture());

        return userMapper.toUpdateResponse(user);
    }

    @Transactional(readOnly = true)
    public UserGetPublicResponse getUserForPublicById(Long id) {
        UserEntity user = findUserOrThrow(id);

        return userMapper.toGetPublicResponse(user);
    }

    @Transactional(readOnly = true)
    public UserGetResponse getUserById(Long id) {
        log.info("Getting user by id {}", id);
        UserEntity user = findUserOrThrow(id);

        return userMapper.toGetResponse(user);
    }

    @Transactional(readOnly = true)
    public Boolean existsUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    private UserEntity findUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username {}", username);
                    return new UserNotFoundException("User not found!");
                });
    }

    private UserEntity findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id {}", id);
                    return new UserNotFoundException("User not found!");
                });
    }
}
