package org.example.todotravel.domain.user.service;

import org.example.todotravel.domain.user.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    void saveRefreshToken(RefreshToken refreshToken);

    Optional<RefreshToken> getRefreshTokenByUserId(Long userId);

    void removeRefreshToken(Long userId);
}
