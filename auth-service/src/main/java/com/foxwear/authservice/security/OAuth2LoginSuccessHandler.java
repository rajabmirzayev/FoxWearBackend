package com.foxwear.authservice.security;

import com.foxwear.authservice.entity.UserEntity;
import com.foxwear.authservice.repository.UserRepository;
import com.foxwear.common.enums.Gender;
import com.foxwear.common.enums.UserStatus;
import com.foxwear.common.enums.Role;
import com.foxwear.common.service.JwtService;
import com.foxwear.authservice.service.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        UserEntity user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    UserEntity newUser = UserEntity.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .email(email)
                            .username(email)
                            .gender(Gender.UNKNOWN)
                            .role(Role.USER)
                            .status(UserStatus.ACTIVE)
                            .isEmailVerified(true)

                            .password(UUID.randomUUID().toString())
                            .build();

                    return userRepository.save(newUser);
                });

        String jwtToken = jwtService.generateToken(user, user.getId());
        String refreshToken = refreshTokenService.createRefreshToken(user).getToken();

        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/callback")
                .queryParam("token", jwtToken)
                .queryParam("refreshToken",refreshToken)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request,response,targetUrl);
    }
}
