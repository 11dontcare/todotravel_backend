package org.example.todotravel.domain.user.repository;

import org.example.todotravel.domain.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // 사용자의 RefreshToken이 존재하는지 확인
    Optional<RefreshToken> findByUser_UserId(Long userId);
}
