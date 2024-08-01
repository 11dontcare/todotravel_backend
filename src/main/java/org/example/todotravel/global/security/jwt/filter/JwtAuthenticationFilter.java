package org.example.todotravel.global.security.jwt.filter;

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
import org.example.todotravel.global.exception.JwtExceptionCode;
import org.example.todotravel.global.security.CustomUserDetails;
import org.example.todotravel.global.security.jwt.token.JwtAuthenticationToken;
import org.example.todotravel.global.security.jwt.util.JwtTokenizer;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * JWT 토큰 인증 필터 클래스
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;

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
        String token = getToken(request); //accessToken 얻어냄.

        if (StringUtils.hasText(token)) {
            try {
                getAuthentication(token);   // 토큰을 사용하여 인증 설정
            } catch (ExpiredJwtException e) {
                request.setAttribute("exception", JwtExceptionCode.EXPIRED_TOKEN.getCode());
                log.error("Expired Token : {}", token, e);
                throw new BadCredentialsException("Expired token exception", e);
            } catch (UnsupportedJwtException e) {
                request.setAttribute("exception", JwtExceptionCode.UNSUPPORTED_TOKEN.getCode());
                log.error("Unsupported Token: {}", token, e);
                throw new BadCredentialsException("Unsupported token exception", e);
            } catch (MalformedJwtException e) {
                request.setAttribute("exception", JwtExceptionCode.INVALID_TOKEN.getCode());
                log.error("Invalid Token: {}", token, e);
                throw new BadCredentialsException("Invalid token exception", e);
            } catch (IllegalArgumentException e) {
                request.setAttribute("exception", JwtExceptionCode.NOT_FOUND_TOKEN.getCode());
                log.error("Token not found: {}", token, e);
                throw new BadCredentialsException("Token not found exception", e);
            } catch (Exception e) {
                log.error("JWT Filter - Internal Error: {}", token, e);
                throw new BadCredentialsException("JWT filter internal exception", e);
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

        CustomUserDetails userDetails = new CustomUserDetails(
            email,
            username,
            "",
            role // Role 객체를 직접 전달
        );

        Authentication authentication = new JwtAuthenticationToken(authorities, userDetails, null); // 인증 객체 생성
        SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContextHolder 인증 객체 설정
    }

    /**
     * 요청에서 토큰을 추출
     *
     * @param request - 요청 객체
     * @return - JWT 토큰
     */
    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }

        Cookie[] cookies = request.getCookies(); // 쿠키에서 토큰 찾기
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
