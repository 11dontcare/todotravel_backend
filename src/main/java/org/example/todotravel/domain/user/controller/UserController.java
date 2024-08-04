package org.example.todotravel.domain.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.user.dto.request.LoginRequestDto;
import org.example.todotravel.domain.user.dto.request.UserRegisterRequestDto;
import org.example.todotravel.domain.user.dto.request.UsernameRequestDto;
import org.example.todotravel.domain.user.dto.response.LoginResponseDto;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.impl.RefreshTokenServiceImpl;
import org.example.todotravel.domain.user.service.impl.UserServiceImpl;
import org.example.todotravel.global.controller.ApiResponse;
import org.example.todotravel.global.jwt.util.JwtTokenizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final JwtTokenizer jwtTokenizer;

    // 회원가입
    @PostMapping("/signup")
    public ApiResponse<?> registerUser(@Valid @RequestBody UserRegisterRequestDto dto) {
        User newUser = userService.registerNewUser(dto, passwordEncoder);
        return new ApiResponse<>(true, "회원가입 성공", newUser);
    }

    // 사용자 아이디 중복 검사
    @PostMapping("/check-username")
    public ApiResponse<?> checkUsername(@RequestParam String username) {
        userService.checkDuplicateUsername(username);
        return new ApiResponse<>(true, "아이디 사용 가능", username);
    }

    // 사용자 이메일 중복 검사
    @PostMapping("/check-email")
    public ApiResponse<?> checkEmail(@RequestParam String email) {
        userService.checkDuplicateEmail(email);
        return new ApiResponse<>(true, "이메일 사용 가능", email);
    }

    // 사용자 닉네임 중복 검사
    @PostMapping("/check-nickname")
    public ApiResponse<?> checkNickname(@RequestParam String nickname) {
        userService.checkDuplicateUsername(nickname);
        return new ApiResponse<>(true, "닉네임 사용 가능", nickname);
    }

    // 로그인
    @PostMapping("/login")
    public ApiResponse<?> login(@Valid @RequestBody LoginRequestDto dto,
                                HttpServletResponse response) {
        User loginUser = userService.checkLoginAvailable(dto.getUsername(), dto.getPassword(), passwordEncoder);

        // accessToken, refreshToken 발급
        jwtTokenizer.issueTokenAndSetCookies(response, loginUser);

        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
            .userId(loginUser.getUserId())
            .nickname(loginUser.getNickname())
            .role(loginUser.getRole().name())
            .build();

        return new ApiResponse<>(true, "로그인 성공", loginResponseDto);
    }

    // 아이디 찾기
    @PostMapping("/find-username")
    public ApiResponse<?> findUsername(@Valid @RequestBody UsernameRequestDto dto) {
        String username = userService.getUsername(dto);
        return new ApiResponse<>(true, "아이디 찾기 성공", username);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ApiResponse<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken") || cookie.getName().equals("refreshToken")) {
                    invalidateCookie(cookie, response);
                }
            }
        }

        return new ApiResponse<>(true, "로그아웃 성공", null);
    }

    // accessToken, refreshToken 제거 (refreshToken은 DB에서도 제거)
    private void invalidateCookie(Cookie cookie, HttpServletResponse response) {
        if (cookie.getName().equals("refreshToken")) {
            refreshTokenService.deleteRefreshToken(cookie.getValue());
            return;
        }
        cookie.setValue("");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
