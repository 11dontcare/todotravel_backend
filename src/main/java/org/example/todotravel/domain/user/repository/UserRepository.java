package org.example.todotravel.domain.user.repository;

import org.example.todotravel.domain.user.entity.SocialType;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByNameAndEmail(String name, String email);

    Optional<User> findByNameAndBirthDateAndEmail(String name, LocalDate birthDate, String email);

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    Optional<User> findUserIdByEmail(String email);

    void deleteByUserId(Long userId);
}
