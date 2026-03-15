package com.rgb.foxwear.service.auth;

import com.rgb.foxwear.entity.auth.UserEntity;
import com.rgb.foxwear.enums.UserStatus;
import com.rgb.foxwear.exception.UserNotFoundException;
import com.rgb.foxwear.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Username not found!"));

        if (user.getStatus() == UserStatus.PENDING) {
            throw new DisabledException("Email is not verified!");
        }

        if (user.getStatus() == UserStatus.BANNED) {
            throw new LockedException("Account is banned!");
        }

        return user;
    }
}
