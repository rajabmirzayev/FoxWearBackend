package com.foxwear.authservice.service;

import com.foxwear.authservice.dto.request.UserFilterRequest;
import com.foxwear.authservice.dto.request.UserUpdateRequest;
import com.foxwear.authservice.dto.response.UserGetAllResponse;
import com.foxwear.authservice.dto.response.UserGetPublicResponse;
import com.foxwear.authservice.dto.response.UserGetResponse;
import com.foxwear.authservice.dto.response.UserUpdateResponse;
import com.foxwear.authservice.entity.UserEntity;
import com.foxwear.authservice.exception.UserNotFoundException;
import com.foxwear.authservice.mapper.UserMapper;
import com.foxwear.authservice.repository.UserRepository;
import com.foxwear.authservice.repository.spesification.UserSpecification;
import com.foxwear.common.enums.Gender;
import com.foxwear.common.enums.Role;
import com.foxwear.common.enums.UserStatus;
import com.foxwear.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

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
        checkUserIdIsNull(id);
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
     * Retrieves a paginated list of users based on filter criteria.
     *
     * @param filter The filtering and pagination parameters.
     * @param userId The ID of the user performing the request.
     * @return A page of UserGetAllResponse objects.
     */
    @Transactional(readOnly = true)
    public Page<UserGetAllResponse> getAllUsers(UserFilterRequest filter, Long userId) {
        log.info("Fetching all users with filter: {}", filter);
        checkUserIdIsNull(userId);

        Pageable pageable = PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                Sort.by(filter.getDirection(), filter.getSortBy())
        );

        Page<UserEntity> users = userRepository.findAll(buildUserSpecification(filter), pageable);

        return users.map(userMapper::toGetAllResponse);
    }

    /**
     * Retrieves a list of all available user statuses.
     *
     * @return A list of status names as strings.
     */
    @Transactional(readOnly = true)
    public List<String> getAllStatuses() {
        log.info("Fetching all available user statuses");
        return Arrays.stream(UserStatus.values())
                .map(Enum::name)
                .toList();
    }

    /**
     * Retrieves a list of all available genders.
     *
     * @return A list of gender names as strings.
     */
    @Transactional(readOnly = true)
    public List<String> getAllGenders() {
        log.info("Fetching all available genders");
        return Arrays.stream(Gender.values())
                .map(Enum::name)
                .toList();
    }

    /**
     * Retrieves a list of all available user roles.
     *
     * @return A list of role names as strings.
     */
    @Transactional(readOnly = true)
    public List<String> getAllRoles() {
        log.info("Fetching all available roles");
        return Arrays.stream(Role.values())
                .map(Enum::name)
                .toList();
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
     * Updates the role of a specific user.
     *
     * @param id   The ID of the user whose role is to be updated.
     * @param role The new role to assign to the user.
     */
    @Transactional
    public void changeUserRole(Long id, Role role) {
        log.info("Changing user role for user ID: {} to {}", id, role.name());
        UserEntity user = findUserOrThrow(id);
        user.setRole(role);        userRepository.save(user);
        log.info("User role updated successfully for user ID: {}", id);
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

    /**
     * Validates that the provided user ID is not null.
     *
     * @param userId The ID to validate.
     * @throws UnauthorizedException if the userId is null.
     */
    private void checkUserIdIsNull(Long userId) {
        if (userId == null) {
            throw new UnauthorizedException("Unauthorized user!");
        }
    }

    /**
     * Builds a dynamic JPA Specification for filtering users based on various criteria.
     *
     * @param filter The filter request containing search criteria.
     * @return A Specification object for UserEntity.
     */
    private Specification<UserEntity> buildUserSpecification(UserFilterRequest filter) {
        return Specification.where(UserSpecification.hasGender(filter.getGenders()))
                .and(UserSpecification.hasRole(filter.getRoles()))
                .and(UserSpecification.hasStatus(filter.getStatuses()))
                .and(UserSpecification.isEmailVerified(filter.getIsEmailVerified()))
                .and(UserSpecification.isPhoneNumberVerified(filter.getIsPhoneNumberVerified()))
                .and(UserSpecification.isTwoFactorEnabled(filter.getTwoFactorEnabled()))
                .and(UserSpecification.search(filter.getSearchKeyword()));
    }

}
