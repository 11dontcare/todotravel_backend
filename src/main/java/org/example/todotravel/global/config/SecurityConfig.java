package org.example.todotravel.global.config;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.global.exception.CustomAuthenticationEntryPoint;
import org.example.todotravel.global.jwt.filter.JwtAuthenticationFilter;
import org.example.todotravel.global.jwt.util.JwtTokenizer;
import org.example.todotravel.global.oauth2.handler.OAuth2LoginFailureHandler;
import org.example.todotravel.global.oauth2.handler.OAuth2LoginSuccessHandler;
import org.example.todotravel.global.oauth2.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정 클래스
 * 애플리케이션의 보안 구성을 정의
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenizer jwtTokenizer;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    // 모든 유저 허용 URL
    String[] allAllowPage = new String[]{
            "/api/plan/**", // 우선 "/api/plan/**" 전부 허용해두었습니다.
            "/index.html", // 프론트 구현 완료시 삭제
            "/api/location/**",
            "/api/invite/**",
            "/api/chat/**",
            "/api/notification/**",
            "/api/mypage/**",
            "/api/token/refresh",
            "/api/recruitment/**",
            "/api/auth/logout",
            "/api/auth/profile-image/**",
            "/ws/**",
    };

    // 비로그인 유저 허용 URL
    String[] notLoggedAllowPage = new String[]{
        "/api/auth/signup",
        "/api/auth/check-username",
        "/api/auth/check-email",
        "/api/auth/check-nickname",
        "/api/auth/login",
        "/api/auth/find-username",
        "/api/auth/password-reset",
        "/api/auth/oauth2/**",
        "/oauth2/**",
        "/api/send-mail/**",
        "/api/notification/**",
    };

    /**
     * 보안 필터 체인
     *
     * @param http - 수정할 HttpSecurity 객체
     * @return 구성된 SecurityFilterChain
     * @throws Exception HttpSecurity 구성 시 발생한 예외
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            /* 유저별 URL 접근 허용 */
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(allAllowPage).permitAll()  // 모든 유저
                .requestMatchers(notLoggedAllowPage).not().authenticated() // 비로그인 유저
                .anyRequest().authenticated()
            )

            /* rest api 설정 */
            .csrf(csrf -> csrf.disable()) // JWT 사용으로 csrf 비활성화 -> 쿠키 자체 설정
            .httpBasic(auth -> auth.disable()) // 기본 인증 로그인 비활성화
            .formLogin(auth -> auth.disable()) // 기본 login form 비활성화
            .logout(auth -> auth.disable()) // 기본 logout 비활성화
            .sessionManagement(auth -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 서버가 클라이언트 상태 저장 X

            /* jwt 관련 설정 */
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenizer), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(customAuthenticationEntryPoint)
            )

            /* oauth 관련 설정 */
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(endpoint -> endpoint.baseUri("/oauth2/authorize"))
                .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/**")) // oauth 응답 url
                .userInfoEndpoint(endpoint -> endpoint.userService(customOAuth2UserService)) // oauth User에 대한 서비스
                .successHandler(oAuth2LoginSuccessHandler) // oauth 로그인 성공시의 핸들러
                .failureHandler(oAuth2LoginFailureHandler)
            )
        ;

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
