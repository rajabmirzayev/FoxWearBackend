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

    /**
     * Updates user profile information.
     *
     * @param request The updated user details.
     * @param id      The ID of the user to update.
     * @return UserUpdateResponse containing updated data.
     */
    @Transactional
    public UserUpdateResponse updateUser(UserUpdateRequest request, Long id) {
        if (id == null) {
            throw new UnauthorizedException("Unauthorized user!");
        }
        log.info("Updating user profile for user ID: {}", id);

        UserEntity user = findUserOrThrow(id);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setGender(request.getGender());
        user.setBirthDate(request.getBirthDate());
        user.setProfilePicture(request.getProfilePicture());

        log.info("User profile updated successfully for user ID: {}", id);
        return userMapper.toUpdateResponse(user);
    }

    /**
     * Retrieves public profile information for a user.
     *
     * @param id The ID of the user.
     * @return UserGetPublicResponse containing public data.
     */
    @Transactional(readOnly = true)
    public UserGetPublicResponse getUserForPublicById(Long id) {
        log.info("Fetching public profile for user ID: {}", id);
        UserEntity user = findUserOrThrow(id);

        return userMapper.toGetPublicResponse(user);
    }

    /**
     * Retrieves full profile information for a user.
     *
     * @param id The ID of the user.
     * @return UserGetResponse containing full user data.
     */
    @Transactional(readOnly = true)
    public UserGetResponse getUserById(Long id) {
        log.info("Fetching full profile for user ID: {}", id);
        UserEntity user = findUserOrThrow(id);

        return userMapper.toGetResponse(user);
    }

    /**
     * Checks if a username is already taken.
     *
     * @param username The username to check.
     * @return Boolean true if exists, false otherwise.
     */
    @Transactional(readOnly = true)
    public Boolean existsUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Finds a user by email or throws UserNotFoundException.
     */
    public UserEntity findUserOrThrow(String email) {
        log.debug("Finding user by email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found with email {}", email);
                    return new UserNotFoundException("User not found!");
                });
    }

    /**
     * Finds a user by ID or throws UserNotFoundException.
     */
    public UserEntity findUserOrThrow(Long id) {
        log.debug("Finding user by ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id {}", id);
                    return new UserNotFoundException("User not found!");
                });
    }

}
