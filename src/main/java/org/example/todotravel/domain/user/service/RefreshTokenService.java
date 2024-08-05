package org.example.todotravel.domain.user.service;

import org.example.todotravel.domain.user.entity.RefreshToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RefreshTokenService {
    void saveRefreshToken(RefreshToken refreshToken);

    Optional<RefreshToken> getRefreshTokenByUserId(Long userId);

    void deleteRefreshToken(Long userId);
}
