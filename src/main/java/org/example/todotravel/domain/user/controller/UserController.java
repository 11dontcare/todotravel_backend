package org.example.todotravel.domain.user.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todotravel.domain.user.dto.request.*;
import org.example.todotravel.domain.user.dto.response.LoginResponseDto;
import org.example.todotravel.domain.user.dto.response.UsernameResponseDto;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.impl.UserServiceImpl;
import org.example.todotravel.global.controller.ApiResponse;
import org.example.todotravel.global.jwt.util.JwtTokenizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;

    // 회원가입
    @PostMapping("/signup")
    public ApiResponse<?> registerUser(@Valid @RequestBody UserRegisterRequestDto dto) {
        userService.registerNewUser(dto, passwordEncoder);
        return new ApiResponse<>(true, "회원가입을 완료했습니다.");
    }

    // OAuth2 첫 가입시 추가 정보 입력 후 로그인 처리
    @PostMapping("/oauth2/additional-info")
    public ApiResponse<?> updateOAuth2UserAdditionalInfo(@RequestBody OAuth2AdditionalInfoRequestDto dto,
                                                         HttpServletResponse response) {
        try {
            User updatedUser = userService.updateOAuth2UserAdditionalInfo(dto);

            // accessToken, refreshToken 생성
            String accessToken = jwtTokenizer.issueTokenAndSetCookies(response, updatedUser);

            LoginResponseDto loginResponseDto = LoginResponseDto.of(updatedUser, accessToken);
            return new ApiResponse<>(true, "추가 정보 업데이트에 성공했습니다.", loginResponseDto);
        } catch (Exception e) {
            return new ApiResponse<>(false, "추가 정보 업데이트에 실패했습니다.");
        }
    }

    // OAuth2 기존 가입 유저 로그인
    @GetMapping("/oauth2/login")
    public ApiResponse<?> oauth2UserLogin(@RequestParam("token") String userInfoJwt, HttpServletResponse response) {
        try {
            Claims claims = jwtTokenizer.parseAccessToken(userInfoJwt);
            String email = claims.getSubject();
            User user = userService.getUserByEmail(email);

            // accessToken, refreshToken 생성
            String accessToken = jwtTokenizer.issueTokenAndSetCookies(response, user);

            LoginResponseDto loginResponseDto = LoginResponseDto.of(user, accessToken);
            return new ApiResponse<>(true, "OAuth2 로그인을 성공했습니다.", loginResponseDto);
        } catch (Exception e) {
            return new ApiResponse<>(false, "OAuth2 로그인을 실패했습니다. : " + e.getMessage(), null);
        }
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
    public ApiResponse<?> login(@Valid @RequestBody LoginRequestDto dto, HttpServletResponse response) {
        User loginUser = userService.checkLoginAvailable(dto.getUsername(), dto.getPassword(), passwordEncoder);

        // accessToken, refreshToken 생성
        String accessToken = jwtTokenizer.issueTokenAndSetCookies(response, loginUser);

        LoginResponseDto loginResponseDto = LoginResponseDto.of(loginUser, accessToken);
        return new ApiResponse<>(true, "로그인을 성공했습니다.", loginResponseDto);
    }

    // 아이디 찾기 인증확인
    @PostMapping("/find-username")
    public ApiResponse<?> findUsername(@Valid @RequestBody UsernameRequestDto dto) {
        UsernameResponseDto usernameResponseDto = userService.getUsername(dto);
        return new ApiResponse<>(true, "아이디 찾기를 성공했습니다.", usernameResponseDto);
    }

    // 비밀번호 재설정
    @PostMapping("/password-reset")
    public ApiResponse<?> resetPassword(@RequestBody PasswordResetRequestDto dto) {
        try {
            userService.renewPassword(dto, passwordEncoder);
            return new ApiResponse<>(true, "비밀번호 재설정을 완료했습니다.");
        } catch (Exception e) {
            return new ApiResponse<>(false, "비밀번호 재설정을 실패했습니다.");
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public ApiResponse<?> logout(HttpServletRequest request, HttpServletResponse response) {
        jwtTokenizer.deleteRefreshTokenCookie(request, response);
        jwtTokenizer.deleteRefreshTokenFromDB(request);

        // 클라이언트에게 AccessToken 삭제 지시 (프론트엔드에서 처리)
        return new ApiResponse<>(true, "로그아웃을 성공했습니다.");
    }
}
