package org.example.todotravel.global.oauth2.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMessage;
        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2AuthenticationException oauth2Exception = (OAuth2AuthenticationException) exception;
            String errorCode = oauth2Exception.getError().getErrorCode();
            if (errorCode.equals("duplicate_social_account")) {
                errorMessage = URLEncoder.encode("이미 다른 소셜 계정으로 가입된 이메일입니다.", StandardCharsets.UTF_8);
            } else {
                errorMessage = URLEncoder.encode(oauth2Exception.getError().getDescription(), StandardCharsets.UTF_8);
            }
        } else {
            errorMessage = URLEncoder.encode("소셜 로그인 중 오류가 발생했습니다.", StandardCharsets.UTF_8);
        }

        log.error("OAuth2 로그인 실패 - {}", exception.getMessage());

        String frontendUrl = "http://localhost:3000"; // 프론트엔드 URL, 추후 환경변수로 변경 필요
        String redirectUrl = frontendUrl + "/oauth2/redirect?error=" + errorMessage;
        response.sendRedirect(redirectUrl);
    }
}
