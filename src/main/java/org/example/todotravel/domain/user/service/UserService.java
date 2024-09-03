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

    // 일반 회원가입 유저 등록
    User registerNewUser(UserRegisterRequestDto dto, PasswordEncoder passwordEncoder);

    // OAuth2 회원가입 유저 등록
    User loginOAuth2User(OAuth2UserLoginRequestDto dto);

    // OAuth2 가입 유저 추가 정보 업데이트
    User updateOAuth2UserAdditionalInfo(OAuth2AdditionalInfoRequestDto dto);

    // 사용자 아이디 중복 검사
    void checkDuplicateUsername(String username);

    // 사용자 이메일 중복 검사
    void checkDuplicateEmail(String email);

    // 사용자 닉네임 중복 검사
    void checkDuplicateNickname(String nickname);

    // 로그인 가능한지 확인
    User checkLoginAvailable(String username, String password, PasswordEncoder passwordEncoder);

    // 소개글 업데이트
    void updateUserInfo(User user, UserInfoRequestDto dto);

    // 비밀번호 재설정
    void renewPassword(PasswordResetRequestDto dto, PasswordEncoder passwordEncoder);

    // 비밀번호 재설정 - 기존 비밀번호 검사도 수행
    void updatePassword(User user, PasswordUpdateRequestDto dto, PasswordEncoder passwordEncoder);

    // 이름과 이메일로 아이디 혹은 이메일 찾기
    Object getUsernameOrEmail(UsernameRequestDto dto);

    // 이메일로 사용자 찾기
    User getUserByEmail(String email);

    // userId로 사용자 찾기
    User getUserById(Long userId);

    // 닉네임으로 사용자 찾기
    User getUserIdByNickname(String nickname);

    // 이름, 생년월일, 이메일로 사용자 찾기
    PasswordSearchResponseDto getUserByNameAndBirthAndEmail(PasswordSearchRequestDto dto);

    // 닉네임 업데이트
    void updateNickname(User user, String newNickname);

    // 플랜에 사용자 초대 시 모든 사용자 목록을 return - 김민정
    List<User> getAllUsers();

    // 로그인 중인 사용자 정보
    User getUserByUsername(String username);

    // 사용자 이미지 프로필 업로드
    void updateProfileImage(Long userId, MultipartFile file);

    // 사용자 프로필 이미지 조회
    User getProfileImageUrl(Long userId);

    // 회원 탈퇴 시 사용자 정보 모두 삭제
    void removeUser(User user);
}
