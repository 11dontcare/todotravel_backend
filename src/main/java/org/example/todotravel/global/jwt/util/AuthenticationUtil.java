package org.example.todotravel.global.jwt.util;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.UserService;
import org.example.todotravel.global.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationUtil {
    private final UserService userService;

    public boolean isAuthenticatedUser(Authentication authentication, Long userId) {
        if (authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userService.getUserByEmail(userDetails.getEmail());
            return user.getUserId().equals(userId);
        }
        return false;
    }
}
