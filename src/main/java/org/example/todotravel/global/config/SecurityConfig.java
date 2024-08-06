package org.example.todotravel.global.config;

import lombok.RequiredArgsConstructor;
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
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

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

    // 모든 유저 허용 URI
    String[] allAllowPage = new String[]{
        "api/plan/**"
        // 우선 "/api/plan/**" 전부 허용해두었습니다.
    };

    // 비로그인 유저 허용 URI
    String[] notLoggedAllowPage = new String[]{
        "/api/auth/**",
        "/api/send-mail/**"
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
            /* 유저별 URI 접근 허용 */
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(allAllowPage).permitAll()  // 모든 유저
                .requestMatchers(notLoggedAllowPage).not().authenticated() // 비로그인 유저
                .anyRequest().authenticated()
            )

            /* rest api 설정 */
            .csrf(csrf -> csrf.disable()) // JWT 사용으로 csrf 비활성화 -> 쿠키 자체 설정
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));            // 허용된 출처(Origin)
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));     // 메서드 허용
                configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));    // 헤더 허용
                configuration.setAllowCredentials(true);    // 인증 정보(쿠키 등)를 포함할 수 있도록
                configuration.setMaxAge(3600L);             // CORS 프리플라이트 요청의 캐시 시간을 1시간으로 설정
                return configuration;
            }))
            .httpBasic(auth -> auth.disable()) // 기본 인증 로그인 비활성화
            .formLogin(auth -> auth.disable()) // 기본 login form 비활성화
            .logout(auth -> auth.disable()) // 기본 logout 비활성화
            .sessionManagement(auth -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 서버가 클라이언트 상태 저장 X

            /* jwt 관련 설정 */
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenizer), UsernamePasswordAuthenticationFilter.class)

            /* oauth 관련 설정 */
            .oauth2Login(oauth2 -> oauth2
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
