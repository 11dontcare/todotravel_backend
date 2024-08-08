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
import org.example.todotravel.global.oauth2.CustomOAuth2User;
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
    private final byte[] accessSecret;
    private final byte[] refreshSecret;

    public static Long ACCESS_TOKEN_EXPIRATION_COUNT = 30 * 60 * 1000L;     // 30분
    public static Long REFRESH_TOKEN_EXPIRATION_COUNT = 14 * 24 * 60 * 60 * 1000L;   // 14일

    public JwtTokenizer(@Value("${jwt.secretKey}") String accessSecret,
                        @Value("${jwt.refreshKey}") String refreshSecret,
                        RefreshTokenServiceImpl refreshTokenService) {
        this.accessSecret = accessSecret.getBytes(StandardCharsets.UTF_8);
        this.refreshSecret = refreshSecret.getBytes(StandardCharsets.UTF_8);
        this.refreshTokenService = refreshTokenService;
    }

    public String createAccessToken(User user) {
        return createToken(
            user.getUserId(), user.getUsername(), user.getEmail(), user.getRole(), ACCESS_TOKEN_EXPIRATION_COUNT, accessSecret
        );
    }

    public String createRefreshToken(User user) {
        return createToken(
            user.getUserId(), user.getUsername(), user.getEmail(), user.getRole(), REFRESH_TOKEN_EXPIRATION_COUNT, refreshSecret
        );
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
     * @return 클라이언트에게 넘겨줄 AccessToken
     */
    public String issueTokenAndSetCookies(HttpServletResponse response, User user) {
        // 토큰 생성
        String accessToken = createAccessToken(user);
        String refreshToken = createRefreshToken(user);

        // 리프레쉬 토큰을 DB에 저장
        RefreshToken refreshTokenEntity = refreshTokenService.getRefreshTokenByUserId(user.getUserId())
            .orElseGet(() -> {
                RefreshToken newToken = new RefreshToken();
                newToken.setUser(user);
                return newToken;
            });
        refreshTokenEntity.setValue(refreshToken);
        refreshTokenService.saveRefreshToken(refreshTokenEntity);

        addTokenCookie(response, refreshToken, REFRESH_TOKEN_EXPIRATION_COUNT);

        return accessToken;
    }

    /**
     * refreshToken 쿠키 생성 메서드
     *
     * @param response       응답 객체
     * @param tokenValue     토큰 값
     * @param expirationTime 토큰 만료 시간
     */
    private void addTokenCookie(HttpServletResponse response, String tokenValue, Long expirationTime) {
        Cookie tokenCookie = new Cookie("refreshToken", tokenValue);
        tokenCookie.setPath("/");
        tokenCookie.setHttpOnly(true);
        tokenCookie.setMaxAge(Math.toIntExact(expirationTime / 1000)); // milliseconds to seconds
        response.addCookie(tokenCookie);
    }

    /**
     * refreshTOken 유효성 검증
     *
     * @param refreshToken 토큰 값
     * @return 유효성 결과
     */
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey(refreshSecret)).build().parseClaimsJws(refreshToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String createTempJwtForOAuth2User(CustomOAuth2User oAuth2User) {
        Claims claims = Jwts.claims().setSubject(oAuth2User.getEmail());
        log.info("oAuth2User.getName:: {}", oAuth2User.getName());
        claims.put("name", oAuth2User.getName());

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // 5분 유효
            .signWith(getSigningKey(accessSecret))
            .compact();
    }
}
