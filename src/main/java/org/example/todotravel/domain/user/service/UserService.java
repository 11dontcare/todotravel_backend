package org.example.todotravel.domain.user.service;

import org.example.todotravel.domain.user.dto.request.*;
import org.example.todotravel.domain.user.dto.response.*;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    Optional<User> getUserByUserId(Long userId);

    User registerNewUser(UserRegisterRequestDto dto, PasswordEncoder passwordEncoder);

    User loginOAuth2User(OAuth2UserLoginRequestDto dto);

    void checkDuplicateUsername(String username);

    void checkDuplicateEmail(String email);

    void checkDuplicateNickname(String nickname);

    User checkLoginAvailable(String username, String password, PasswordEncoder passwordEncoder);

    UsernameResponseDto getUsername(UsernameRequestDto dto);

    void updateUserInfo(User user, UserInfoRequestDto dto);

    void renewPassword(PasswordResetRequestDto dto, PasswordEncoder passwordEncoder);

    void updatePassword(User user, PasswordUpdateRequestDto dto, PasswordEncoder passwordEncoder);

    PasswordSearchResponseDto findUserByNameAndBirthAndEmail(PasswordSearchRequestDto dto);

    OAuth2SignUpResponseDto getUserIdByEmail(String email);

    User updateOAuth2UserAdditionalInfo(OAuth2AdditionalInfoRequestDto dto);

    User getUserByEmail(String email);

    User getUserIdByNickname(String nickname);

    User getUserById(Long userId);

    void updateNickname(User user, String newNickname);

    //플랜에 사용자 초대 시 모든 사용자 목록을 return - 김민정
    List<User> getAllUsers();

    //로그인 중인 사용자 정보
    User getUserByUsername(String username);

    // 사용자 이미지 프로필 업로드
    void updateProfileImage(Long userId, MultipartFile file);

    User getProfileImageUrl(Long userId);
}
