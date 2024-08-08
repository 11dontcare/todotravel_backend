package org.example.todotravel.domain.user.controller;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.UserService;
import org.example.todotravel.global.controller.ApiResponse;
import org.example.todotravel.global.exception.UserNotFoundException;
import org.example.todotravel.global.jwt.util.JwtTokenizer;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenController {
    private final JwtTokenizer jwtTokenizer;
    private final UserService userService;

    @PostMapping("/refresh")
    public ApiResponse<?> refreshToken(@CookieValue(name = "refreshToken") String refreshToken) {
        if (jwtTokenizer.validateRefreshToken(refreshToken)) {
            Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);
            Long userId = Long.valueOf((Integer) claims.get("userId"));
            User user = userService.getUserByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

            String newAccessToken = jwtTokenizer.createAccessToken(user);

            return new ApiResponse<>(true, "토큰 갱신 성공", Collections.singletonMap("accessToken", newAccessToken));
        }
        return new ApiResponse<>(false, "토큰 갱신 실패", null);
    }
}
