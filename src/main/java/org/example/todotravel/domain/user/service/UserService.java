package org.example.todotravel.domain.user.service;

import org.example.todotravel.domain.user.dto.request.*;
import org.example.todotravel.domain.user.dto.response.OAuth2SignUpResponseDto;
import org.example.todotravel.domain.user.dto.response.PasswordSearchResponseDto;
import org.example.todotravel.domain.user.dto.response.UsernameResponseDto;
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

    UsernameResponseDto getUsername(UsernameRequestDto dto);

    void renewPassword(PasswordResetRequestDto dto, PasswordEncoder passwordEncoder);

    PasswordSearchResponseDto findUserByNameAndBirthAndEmail(PasswordSearchRequestDto dto);

    OAuth2SignUpResponseDto getUserIdByEmail(String email);

    User updateOAuth2UserAdditionalInfo(OAuth2AdditionalInfoRequestDto dto);

    User getUserByEmail(String email);

    User getUserById(Long userId);

    //플랜에 사용자 초대 시 모든 사용자 목록을 return - 김민정
    List<User> getAllUsers();
}
