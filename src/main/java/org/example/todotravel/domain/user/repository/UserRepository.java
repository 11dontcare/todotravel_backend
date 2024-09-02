package org.example.todotravel.domain.user.repository;

import org.example.todotravel.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 아이디로 사용자 찾기
    Optional<User> findByUsername(String username);

    // 이메일로 사용자 찾기
    Optional<User> findByEmail(String email);

    // 닉네임으로 사용자 찾기
    Optional<User> findByNickname(String nickname);

    // 닉네임과 이메일로 사용자 찾기
    Optional<User> findByNameAndEmail(String name, String email);

    // 이름, 생년월일, 이메일로 사용자 찾기
    Optional<User> findByNameAndBirthDateAndEmail(String name, LocalDate birthDate, String email);

    // 이메일로 userId 찾기
    Optional<User> findUserIdByEmail(String email);

    // userId로 사용자 삭제
    void deleteByUserId(Long userId);
}
