package org.example.todotravel.domain.user.service;

import org.example.todotravel.domain.user.entity.RefreshToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RefreshTokenService {
    public void saveRefreshToken(RefreshToken refreshToken);

    public Optional<RefreshToken> getRefreshTokenByUserId(Long userId);

    public void deleteRefreshToken(String refreshToken);
}
