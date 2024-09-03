package org.example.todotravel.domain.user.service.impl;

import org.example.todotravel.domain.user.entity.RefreshToken;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.repository.RefreshTokenRepository;
import org.example.todotravel.domain.user.service.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private RefreshToken refreshToken;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
            .userId(1L)
            .username("testUser")
            .build();

        refreshToken = RefreshToken.builder()
            .refreshTokenId(1L)
            .user(user)
            .value("refreshTokenValue")
            .build();
    }

    @Test
    @DisplayName("리프레시 토큰 저장 테스트")
    void saveRefreshToken() {
        // given
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);

        // when
        refreshTokenService.saveRefreshToken(refreshToken);

        // then
        verify(refreshTokenRepository, times(1)).save(refreshToken);
    }

    @Test
    @DisplayName("사용자 ID로 리프레시 토큰 조회 테스트")
    void getRefreshTokenByUserId() {
        // given
        when(refreshTokenRepository.findByUser_UserId(1L)).thenReturn(Optional.of(refreshToken));

        // when
        Optional<RefreshToken> result = refreshTokenService.getRefreshTokenByUserId(1L);

        // then
        assertTrue(result.isPresent());
        assertEquals(refreshToken, result.get());
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 리프레시 토큰 조회 테스트")
    void getRefreshTokenByUserId_NotFound() {
        // given
        when(refreshTokenRepository.findByUser_UserId(2L)).thenReturn(Optional.empty());

        // when
        Optional<RefreshToken> result = refreshTokenService.getRefreshTokenByUserId(2L);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("리프레시 토큰 삭제 테스트")
    void removeRefreshToken() {
        // given
        when(refreshTokenRepository.findByUser_UserId(1L)).thenReturn(Optional.of(refreshToken));

        // when
        refreshTokenService.removeRefreshToken(1L);

        // then
        verify(refreshTokenRepository, times(1)).delete(refreshToken);
    }

    @Test
    @DisplayName("존재하지 않는 리프레시 토큰 삭제 시도 테스트")
    void removeRefreshToken_NotFound() {
        // given
        when(refreshTokenRepository.findByUser_UserId(2L)).thenReturn(Optional.empty());

        // when
        refreshTokenService.removeRefreshToken(2L);

        // then
        // 'RefreshToken 타입의 어떤 객체에 대해서도 delete 메서드가 호출되지 않았음'을 검증
        verify(refreshTokenRepository, never()).delete(any(RefreshToken.class));
    }
}