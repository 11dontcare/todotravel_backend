package org.example.todotravel.global.oauth2.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todotravel.domain.user.entity.Role;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.repository.UserRepository;
import org.example.todotravel.global.jwt.util.JwtTokenizer;
import org.example.todotravel.global.oauth2.CustomOAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenizer jwtTokenizer;
    private final UserRepository userRepository;

    // oAuth2 로그인 권한 핸들러
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("OAuth2 Login 성공");

        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            Optional<User> user = userRepository.findByEmail(oAuth2User.getEmail());

            if (oAuth2User.getRole() == Role.ROLE_GUEST) {
                String userInfoJwt = jwtTokenizer.createTempJwtForOAuth2User(oAuth2User);
                String redirectUrl = String.format("/api/auth/oauth2/signup?token=%s", userInfoJwt);
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            } else {
                String userInfoJwt = jwtTokenizer.createTempJwtForOAuth2User(oAuth2User);
                String redirectUrl = String.format("/api/auth/oauth2/login?token=%s", userInfoJwt);
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
