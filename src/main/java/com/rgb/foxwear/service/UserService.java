package com.rgb.foxwear.service;

import com.rgb.foxwear.dto.request.auth.CreateUserRequest;
import com.rgb.foxwear.dto.response.auth.CreateUserResponse;
import com.rgb.foxwear.entity.auth.UserEntity;
import com.rgb.foxwear.entity.ordering.Cart;
import com.rgb.foxwear.exception.PasswordMismatchException;
import com.rgb.foxwear.exception.UnderageUserException;
import com.rgb.foxwear.exception.UserAlreadyExistsException;
import com.rgb.foxwear.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Transactional
    public CreateUserResponse createUser(
            CreateUserRequest request
    ) {
        checkUserExists(request);
        checkPasswordsMatch(request);
        checkUnderage(request.getBirthDate());

        UserEntity user = mapper.map(request, UserEntity.class);

        Cart cart = new Cart();

        user.setCart(cart);

        userRepository.save(user);

        return mapper.map(user, CreateUserResponse.class);
    }

    private void checkPasswordsMatch(CreateUserRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException("Passwords do not match");
        }
    }

    private void checkUserExists(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new UserAlreadyExistsException("Phone number already exists");
        }
    }

    private void checkUnderage(LocalDate date) {
        if (Period.between(date, LocalDate.now()).getYears() < 18) {
            throw new UnderageUserException("The user must be over 18 years old");
        }
    }
}
