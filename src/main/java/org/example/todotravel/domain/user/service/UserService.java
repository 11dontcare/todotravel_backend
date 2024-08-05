package org.example.todotravel.domain.user.service;

import org.example.todotravel.domain.user.dto.request.OAuth2UserLoginRequestDto;
import org.example.todotravel.domain.user.dto.request.UserRegisterRequestDto;
import org.example.todotravel.domain.user.dto.request.UsernameRequestDto;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    Optional<User> getUserByUserId(Long userId);

    User registerNewUser(UserRegisterRequestDto dto, PasswordEncoder passwordEncoder);

    User loginOAuth2User(OAuth2UserLoginRequestDto dto);

    void checkDuplicateUsername(String username);

    void checkDuplicateEmail(String email);

    void checkDuplicateNickname(String nickname);

    User checkLoginAvailable(String username, String password, PasswordEncoder passwordEncoder);

    String getUsername(UsernameRequestDto dto);

    void setTempPassword(String email, String tempPassword, PasswordEncoder passwordEncoder);

    //플랜에 사용자 초대 시 모든 사용자 목록을 return - 김민정
    List<User> getAllUsers();
}
