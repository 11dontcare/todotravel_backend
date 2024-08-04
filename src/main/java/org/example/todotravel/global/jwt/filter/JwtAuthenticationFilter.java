package org.example.todotravel.global.jwt.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todotravel.domain.user.entity.Role;
import org.example.todotravel.global.exception.CustomJwtException;
import org.example.todotravel.global.exception.JwtExceptionCode;
import org.example.todotravel.global.security.CustomUserDetails;
import org.example.todotravel.global.jwt.token.JwtAuthenticationToken;
import org.example.todotravel.global.jwt.util.JwtTokenizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * JWT 토큰 인증 필터 클래스
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;

    String[] notFilter = {
        "/api/auth",
        "/api/send-mail"
    };

    // 인증이 필요 없는 경로는 필터 적용하지 않도록 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return Arrays.stream(notFilter).anyMatch(path::startsWith);
    }

    /**
     * 필터 메서드 - 각 요청마다 JWT 토큰을 검증하고 인증을 설정
     *
     * @param request     요청 객체
     * @param response    응답 객체
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 예외
     * @throws IOException      입출력 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getAccessToken(request); // accessToken 얻어냄.

        if (!StringUtils.hasText(token)) {
            handleTokenRenewal(request, response, false);
        } else {
            try {
                getAuthentication(token);   // 토큰을 사용하여 인증 설정
            } catch (ExpiredJwtException e) {
                handleTokenRenewal(request, response, true);
            } catch (UnsupportedJwtException e) {
                log.error("Unsupported Token: {}", token, e);
                throw new CustomJwtException(JwtExceptionCode.UNSUPPORTED_TOKEN);
            } catch (MalformedJwtException e) {
                log.error("Invalid Token: {}", token, e);
                throw new CustomJwtException(JwtExceptionCode.INVALID_TOKEN);
            } catch (IllegalArgumentException e) {
                log.error("Token not found: {}", token, e);
                throw new CustomJwtException(JwtExceptionCode.NOT_FOUND_TOKEN);
            } catch (Exception e) {
                log.error("JWT Filter - Internal Error: {}", token, e);
                throw new CustomJwtException(JwtExceptionCode.UNKNOWN_ERROR);
            }
        }
        filterChain.doFilter(request, response); // 다음 필터로 요청을 전달
    }

    /**
     * 토큰을 사용하여 인증 설정
     *
     * @param token - JWT 토큰
     */
    private void getAuthentication(String token) {
        Claims claims = jwtTokenizer.parseAccessToken(token); // 토큰에서 클레임을 파싱
        String email = claims.getSubject(); // 이메일을 가져옴
//        Long userId = claims.get("userId", Long.class);
//        String name = claims.get("name", String.class);
        String username = claims.get("username", String.class); // username을 가져옴
        Role role = Role.valueOf(claims.get("roles", String.class)); // 단일 역할을 가져옴

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role.name()));

        CustomUserDetails userDetails = new CustomUserDetails(email, username, "", role);
        Authentication authentication = new JwtAuthenticationToken(authorities, userDetails, null); // 인증 객체 생성
        SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContextHolder 인증 객체 설정
    }

    private String getAccessToken(HttpServletRequest request) {
        return getToken(request, "Authorization", "accessToken");
    }

    private String getRefreshToken(HttpServletRequest request) {
        return getToken(request, "Refresh-Token", "refreshToken");
    }

    /**
     * 지정한 토큰을 반환하는 메서드
     *
     * @param request    요청 객체
     * @param headerName 요청의 헤더 이름
     * @param cookieName 쿠키 이름
     * @return cookieValue
     */
    private String getToken(HttpServletRequest request, String headerName, String cookieName) {
        String authorization = request.getHeader(headerName);
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    /**
     * AccessToken 갱신을 다루는 메서드
     *
     * @param request   요청 객체
     * @param response  응답 객체
     * @param isExpired accessToken 만료 여부
     */
    private void handleTokenRenewal(HttpServletRequest request, HttpServletResponse response, boolean isExpired) {
        String refreshToken = getRefreshToken(request);
        if (refreshToken != null) {
            try {
                jwtTokenizer.renewAccessToken(response, refreshToken);
            } catch (Exception e) {
                log.error("Failed to renew access token", e);
                throw new CustomJwtException(JwtExceptionCode.EXPIRED_TOKEN);
            }
        } else {
            if (isExpired) {
                throw new CustomJwtException(JwtExceptionCode.EXPIRED_TOKEN);
            } else {
                throw new CustomJwtException(JwtExceptionCode.NOT_FOUND_TOKEN);
            }
        }
    }
}
