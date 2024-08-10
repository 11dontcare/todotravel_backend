package org.example.todotravel.global.jwt.util;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import org.example.todotravel.domain.user.entity.RefreshToken;
import org.example.todotravel.domain.user.entity.Role;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.impl.RefreshTokenServiceImpl;
import org.example.todotravel.global.oauth2.CustomOAuth2User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenizerTest {

    private JwtTokenizer jwtTokenizer;

    @Mock
    private RefreshTokenServiceImpl refreshTokenService;

    @BeforeEach
    void setUp() {
        String accessSecret = "testAccessSecret12345678901234567890";
        String refreshSecret = "testRefreshSecret1234567890123456789";
        jwtTokenizer = new JwtTokenizer(accessSecret, refreshSecret, refreshTokenService);
    }

    @Test
    @DisplayName("AccessToken 생성 테스트")
    void createAccessToken() {
        // given
        User user = User.builder()
            .userId(1L)
            .username("testUser")
            .email("test@example.com")
            .role(Role.ROLE_USER)
            .build();

        // when
        String token = jwtTokenizer.createAccessToken(user);

        // then
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("RefreshToken 생성 테스트")
    void createRefreshToken() {
        // given
        User user = User.builder()
            .userId(1L)
            .username("testUser")
            .email("test@example.com")
            .role(Role.ROLE_USER)
            .build();

        // when
        String token = jwtTokenizer.createRefreshToken(user);

        // then
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("토큰에서 유저 아이디 얻기 테스트")
    void getUserIdFromToken() {
        // given
        User user = User.builder()
            .userId(1L)
            .username("testUser")
            .email("test@example.com")
            .role(Role.ROLE_USER)
            .build();
        String token = jwtTokenizer.createAccessToken(user);

        // when
        Long userId = jwtTokenizer.getUserIdFromToken(token);

        // then
        assertEquals(1L, userId);
    }

    @Test
    @DisplayName("AccessToken 파싱 테스트")
    void parseAccessToken() {
        // given
        User user = User.builder()
            .userId(1L)
            .username("testUser")
            .email("test@example.com")
            .role(Role.ROLE_USER)
            .build();
        String token = jwtTokenizer.createAccessToken(user);

        // when
        Claims claims = jwtTokenizer.parseAccessToken(token);

        // then
        assertEquals("test@example.com", claims.getSubject());
        assertEquals(1L, ((Integer) claims.get("userId")).longValue());
        assertEquals("testUser", claims.get("username"));
        assertEquals("ROLE_USER", claims.get("roles"));
    }

    @Test
    @DisplayName("AccessToken, RefreshToken 동시 생성 테스트")
    void issueTokenAndSetCookies() {
        // given
        User user = User.builder()
            .userId(1L)
            .username("testUser")
            .email("test@example.com")
            .role(Role.ROLE_USER)
            .build();
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(refreshTokenService.getRefreshTokenByUserId(1L)).thenReturn(Optional.empty());

        // when
        String accessToken = jwtTokenizer.issueTokenAndSetCookies(response, user);

        // then
        assertNotNull(accessToken);

        // given (RefreshToken 증명을 위해)
        ArgumentCaptor<RefreshToken> refreshTokenCaptor = ArgumentCaptor.forClass(RefreshToken.class);

        // when (RefreshToken 저장 검증)
        verify(refreshTokenService, times(1)).saveRefreshToken(refreshTokenCaptor.capture());
        RefreshToken capturedRefreshToken = refreshTokenCaptor.getValue();

        // then (RefreshToken and cookie 검증)
        assertNotNull(capturedRefreshToken);
        assertEquals(user, capturedRefreshToken.getUser());

        Cookie[] cookies = response.getCookies();
        assertNotNull(cookies);
        assertEquals(1, cookies.length);
        assertEquals("refreshToken", cookies[0].getName());
    }

    @Test
    @DisplayName("RefreshToken 유효성 검증 테스트")
    void validateRefreshToken() {
        // given
        User user = User.builder()
            .userId(1L)
            .username("testUser")
            .email("test@example.com")
            .role(Role.ROLE_USER)
            .build();
        String refreshToken = jwtTokenizer.createRefreshToken(user);

        // when
        boolean isValidToken = jwtTokenizer.validateRefreshToken(refreshToken);
        boolean isInvalidToken = jwtTokenizer.validateRefreshToken("그냥.아무거나.토큰");

        // then
        // 유효한 RefreshToken은 검증을 통과하고, 유효하지 않은 토큰은 실패해야 함
        assertTrue(isValidToken);
        assertFalse(isInvalidToken);
    }

    @Test
    @DisplayName("OAuth2 사용자를 위한 임시 토큰 생성 테스트")
    void createTempJwtForOAuth2User() {
        // given
        CustomOAuth2User oAuth2User = mock(CustomOAuth2User.class);
        when(oAuth2User.getEmail()).thenReturn("oauth@example.com");
        when(oAuth2User.getName()).thenReturn("OAuth User");

        // when
        String token = jwtTokenizer.createTempJwtForOAuth2User(oAuth2User);

        // then
        assertNotNull(token);
        Claims claims = jwtTokenizer.parseAccessToken(token);
        assertEquals("oauth@example.com", claims.getSubject());
        assertEquals("OAuth User", claims.get("name"));
    }
}