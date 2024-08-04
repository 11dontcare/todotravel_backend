package org.example.todotravel.global.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.todotravel.domain.user.entity.RefreshToken;
import org.example.todotravel.domain.user.entity.Role;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.impl.RefreshTokenServiceImpl;
import org.example.todotravel.domain.user.service.impl.UserServiceImpl;
import org.example.todotravel.global.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * JWT 생성 및 검증 유틸리티 클래스
 */
@Slf4j
@Component
public class JwtTokenizer {
    private final RefreshTokenServiceImpl refreshTokenService;
    private final UserServiceImpl userService;
    private final byte[] accessSecret;
    private final byte[] refreshSecret;

    public static Long ACCESS_TOKEN_EXPIRATION_COUNT = 30 * 60 * 1000L;     // 30분
    public static Long REFRESH_TOKEN_EXPIRATION_COUNT = 14 * 24 * 60 * 60 * 1000L;   // 14일

    public JwtTokenizer(@Value("${jwt.secretKey}") String accessSecret,
                        @Value("${jwt.refreshKey}") String refreshSecret,
                        RefreshTokenServiceImpl refreshTokenService,
                        UserServiceImpl userService) {
        this.accessSecret = accessSecret.getBytes(StandardCharsets.UTF_8);
        this.refreshSecret = refreshSecret.getBytes(StandardCharsets.UTF_8);
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
    }

    public String createAccessToken(Long userId, String username, String email, Role role) {
        return createToken(userId, username, email, role, ACCESS_TOKEN_EXPIRATION_COUNT, accessSecret);
    }

    public String createRefreshToken(Long userId, String username, String email, Role role) {
        return createToken(userId, username, email, role, REFRESH_TOKEN_EXPIRATION_COUNT, refreshSecret);
    }

    /**
     * Jwts 빌더를 사용하여 token 생성
     *
     * @param userId    사용자 PK
     * @param username  사용자 아이디
     * @param email     사용자 이메일
     * @param role      사용자 역할
     * @param expire    토큰 만료 기간
     * @param secretKey 토큰 키 값
     * @return Jwts
     */
    private String createToken(Long userId, String username, String email, Role role,
                               Long expire, byte[] secretKey) {
        // 기본으로 가지고 있는 Claim : subject
        Claims claims = Jwts.claims().setSubject(email);

        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("roles", role.name());

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime() + expire))
            .signWith(getSigningKey(secretKey))
            .compact();
    }

    // 토큰에서 유저 아이디 얻기
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token, accessSecret);
        return Long.valueOf((Integer) claims.get("userId"));
    }

    public Claims parseAccessToken(String accessToken) {
        return parseToken(accessToken, accessSecret);
    }

    public Claims parseRefreshToken(String refrestToken) {
        return parseToken(refrestToken, refreshSecret);
    }

    public Claims parseToken(String token, byte[] secretKey) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey(secretKey))
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public static Key getSigningKey(byte[] secretKey) {
        return Keys.hmacShaKeyFor(secretKey);
    }

    /**
     * AccessToken, RefreshToken 동시에 생성
     *
     * @param response 응답 객체
     * @param user     사용자
     */
    public void issueTokenAndSetCookies(HttpServletResponse response, User user) {
        // 토큰 생성
        String accessToken = createAccessToken(user.getUserId(), user.getEmail(), user.getUsername(), user.getRole());
        String refreshToken = createRefreshToken(user.getUserId(), user.getEmail(), user.getUsername(), user.getRole());

        // 리프레쉬 토큰을 DB에 저장
        RefreshToken refreshTokenEntity = refreshTokenService.getRefreshTokenByUserId(user.getUserId())
            .orElseGet(() -> {
                RefreshToken newToken = new RefreshToken();
                newToken.setUser(user);
                return newToken;
            });
        refreshTokenEntity.setValue(refreshToken);
        refreshTokenService.saveRefreshToken(refreshTokenEntity);

        addTokenCookie(response, "accessToken", accessToken, ACCESS_TOKEN_EXPIRATION_COUNT);
        addTokenCookie(response, "refreshToken", refreshToken, REFRESH_TOKEN_EXPIRATION_COUNT);
    }

    /**
     * RefreshToken으로 AccessToken 재발급
     *
     * @param response     응답 객체
     * @param refreshToken refresh token
     */
    public void renewAccessToken(HttpServletResponse response, String refreshToken) {
        // 토큰으로부터 정보 얻기
        Claims claims = parseRefreshToken(refreshToken);
        Long userId = Long.valueOf((Integer) claims.get("userId"));
        User user = userService.getUserByUserId(userId)
            .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));

        // accessToken 생성
        String email = claims.getSubject();
        String accessToken = createAccessToken(userId, email, user.getUsername(), user.getRole());

        addTokenCookie(response, "accessToken", accessToken, ACCESS_TOKEN_EXPIRATION_COUNT);
    }

    /**
     * 쿠키 생성 메서드
     *
     * @param response       응답 객체
     * @param tokenName      토큰 이름
     * @param tokenValue     토큰 값
     * @param expirationTime 토큰 만료 시간
     */
    private void addTokenCookie(HttpServletResponse response, String tokenName, String tokenValue, Long expirationTime) {
        Cookie tokenCookie = new Cookie(tokenName, tokenValue);
        tokenCookie.setPath("/");
        tokenCookie.setHttpOnly(true);
        tokenCookie.setMaxAge(Math.toIntExact(expirationTime / 1000)); // milliseconds to seconds
        response.addCookie(tokenCookie);
    }
}
