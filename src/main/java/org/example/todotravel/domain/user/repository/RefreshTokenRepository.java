package org.example.todotravel.domain.user.repository;

import org.example.todotravel.domain.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByValue(String token);

    Optional<RefreshToken> findByUser_UserId(Long userId);
}
