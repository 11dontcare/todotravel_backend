package org.example.todotravel.domain.user.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.user.dto.request.LoginRequestDto;
import org.example.todotravel.domain.user.dto.request.OAuth2UserLoginRequestDto;
import org.example.todotravel.domain.user.dto.request.UserRegisterRequestDto;
import org.example.todotravel.domain.user.dto.request.UsernameRequestDto;
import org.example.todotravel.domain.user.dto.response.LoginResponseDto;
import org.example.todotravel.domain.user.entity.RefreshToken;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.impl.RefreshTokenServiceImpl;
import org.example.todotravel.domain.user.service.impl.UserServiceImpl;
import org.example.todotravel.global.controller.ApiResponse;
import org.example.todotravel.global.jwt.util.JwtTokenizer;
import org.example.todotravel.global.oauth2.CustomOAuth2User;
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

    // OAuth 추가 로그인
    @PostMapping("/oauth2/signup")
    public ApiResponse<?> oAuth2UserLogin(@Valid @RequestBody OAuth2UserLoginRequestDto dto, HttpServletResponse response) {
        User newOAuth2User = userService.loginOAuth2User(dto);
//        jwtTokenizer.issueTokenAndSetCookies(response, newOAuth2User);
        return new ApiResponse<>(true, "회원가입 성공", newOAuth2User);
    }

    // OAuth2 사용자 정보 조회
    @GetMapping("/oauth2/signup")
    public ApiResponse<?> getOAuth2UserInfo(HttpSession session) {
        CustomOAuth2User oauthUser = (CustomOAuth2User) session.getAttribute("oauthUser");
        if (oauthUser == null) {
            return new ApiResponse<>(false, "User not found", null);
        }
        return new ApiResponse<>(true, "User info", oauthUser);
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
    public ApiResponse<?> login(@Valid @RequestBody LoginRequestDto dto) {
        User loginUser = userService.checkLoginAvailable(dto.getUsername(), dto.getPassword(), passwordEncoder);

        // accessToken, refreshToken 생성
        String accessToken = jwtTokenizer.createAccessToken(loginUser);
        String refreshToken = jwtTokenizer.createRefreshToken(loginUser);

        // refreshToken을 DB에 저장
        RefreshToken refreshTokenEntity = refreshTokenService.getRefreshTokenByUserId(loginUser.getUserId())
            .orElseGet(() -> {
                RefreshToken newToken = new RefreshToken();
                newToken.setUser(loginUser);
                return newToken;
            });
        refreshTokenEntity.setValue(refreshToken);
        refreshTokenService.saveRefreshToken(refreshTokenEntity);

        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
            .userId(loginUser.getUserId())
            .nickname(loginUser.getNickname())
            .role(loginUser.getRole().name())
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .accessTokenExpirationTime(System.currentTimeMillis() + JwtTokenizer.ACCESS_TOKEN_EXPIRATION_COUNT)
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
    public ApiResponse<?> logout(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            // AccessToken에서 사용자 정보 추출
            Claims claims = jwtTokenizer.parseAccessToken(token);
            Long userId = Long.valueOf((Integer) claims.get("userId"));

            // RefreshToken을 DB에서 삭제
            refreshTokenService.deleteRefreshToken(userId);

            return new ApiResponse<>(true, "로그아웃 성공");
        } catch (Exception e) {
            return new ApiResponse<>(false, "로그아웃 실패");
        }


    }
}
