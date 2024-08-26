package org.example.todotravel.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.user.entity.RefreshToken;
import org.example.todotravel.domain.user.repository.RefreshTokenRepository;
import org.example.todotravel.domain.user.service.RefreshTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public Optional<RefreshToken> getRefreshTokenByUserId(Long userId) {
        return refreshTokenRepository.findByUser_UserId(userId);
    }

    @Override
    @Transactional
    public void deleteRefreshToken(Long userId) {
        getRefreshTokenByUserId(userId).ifPresent(refreshTokenRepository::delete);
    }
}
