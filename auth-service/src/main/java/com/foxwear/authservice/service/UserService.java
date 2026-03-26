package com.foxwear.authservice.service;

import com.foxwear.authservice.dto.response.UserGetResponse;
import com.foxwear.authservice.entity.UserEntity;
import com.foxwear.authservice.exception.UserNotFoundException;
import com.foxwear.authservice.mapper.UserMapper;
import com.foxwear.authservice.repository.UserRepository;
import com.foxwear.common.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
//     private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    @Transactional
    public void registerUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserEntity savedUser = userRepository.save(user);

        UserCreatedEvent event = new UserCreatedEvent(savedUser.getId(), savedUser.getEmail());

        // kafkaTemplate.send("user-created-topic", event);
    }

    @Transactional(readOnly = true)
    public UserGetResponse getUserById(Long id) {
        log.info("Getting user by username {}", id);
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
