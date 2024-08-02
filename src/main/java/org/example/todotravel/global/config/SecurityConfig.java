package org.example.todotravel.global.config;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.global.jwt.filter.JwtAuthenticationFilter;
import org.example.todotravel.global.jwt.util.JwtTokenizer;
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

    // 모든 유저 허용 URI
    String[] allAllowPage = new String[]{

    };

    // 비로그인 유저 허용 URI
    String[] notLoggedAlloPage = new String[]{

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
            // 유저별 URI 접근 허용
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll())

            // rest api 설정
            .csrf(csrf -> csrf.disable()) // csrf 비활성화 -> cookie를 사용하지 않으면 꺼도 된다. (cookie를 사용할 경우 httpOnly(XSS 방어), sameSite(CSRF 방어)로 방어해야 한다.)
            .cors(cors -> cors.disable()) // cors 비활성화 -> 프론트와 연결 시 따로 설정 필요
            .httpBasic(auth -> auth.disable()) // 기본 인증 로그인 비활성화
            .formLogin(auth -> auth.disable()) // 기본 login form 비활성화
            .logout(auth -> auth.disable()) // 기본 logout 비활성화
            .sessionManagement(auth -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 서버가 클라이언트 상태 저장 X

            // jwt 관련 설정
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenizer), UsernamePasswordAuthenticationFilter.class)

        ;

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
