package org.example.todotravel.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.user.dto.request.OAuth2UserLoginRequestDto;
import org.example.todotravel.domain.user.dto.request.UserRegisterRequestDto;
import org.example.todotravel.domain.user.dto.request.UsernameRequestDto;
import org.example.todotravel.domain.user.entity.Role;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.repository.UserRepository;
import org.example.todotravel.domain.user.service.UserService;
import org.example.todotravel.global.exception.DuplicateUserException;
import org.example.todotravel.global.exception.UserNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUserId(Long userId) {
        return userRepository.findById(userId);
    }

    // 일반 회원가입 유저 등록
    @Override
    @Transactional
    public User registerNewUser(UserRegisterRequestDto dto, PasswordEncoder passwordEncoder) {
        checkDuplicateUsername(dto.getUsername());
        checkDuplicateEmail(dto.getEmail());
        checkDuplicateNickname(dto.getNickname());

        User newUser = User.builder()
            .username(dto.getUsername())
            .password(passwordEncoder.encode(dto.getPassword()))
            .nickname(dto.getNickname())
            .name(dto.getName())
            .gender(dto.getGender())
            .email(dto.getEmail())
            .createdDate(LocalDateTime.now())
            .birthDate(dto.getBirthDate())
            .role(Role.ROLE_USER)
            .build();

        return userRepository.save(newUser);
    }

    // Oauth2 회원가입 유저 등록
    @Override
    @Transactional
    public User loginOAuth2User(OAuth2UserLoginRequestDto dto) {

        User newOAuth2User = User.builder()
            .gender(dto.getGender())
            .birthDate(dto.getBirthDate())
            .role(Role.ROLE_USER)
            .build();

        return  userRepository.save(newOAuth2User);
    }

    // 사용자 아이디 중복 검사
    @Override
    @Transactional(readOnly = true)
    public void checkDuplicateUsername(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new DuplicateUserException("이미 존재하는 사용자명입니다.");
        }
    }

    // 사용자 이메일 중복 검사
    @Override
    @Transactional(readOnly = true)
    public void checkDuplicateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateUserException("이미 존재하는 이메일입니다.");
        }
    }

    // 사용자 닉네임 중복 검사
    @Override
    @Transactional(readOnly = true)
    public void checkDuplicateNickname(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new DuplicateUserException("이미 존재하는 닉네임입니다.");
        }
    }

    // 로그인 가능한지 확인
    @Override
    @Transactional(readOnly = true)
    public User checkLoginAvailable(String username, String password, PasswordEncoder passwordEncoder) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("존재하지 않는 아이디입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    // 임시 비밀번호 재설정
    @Override
    @Transactional
    public void setTempPassword(String email, String tempPassword, PasswordEncoder passwordEncoder) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));

        user.setPassword(passwordEncoder.encode(tempPassword));

        userRepository.save(user);
    }

    // 이름, 이메일로 아이디 찾기
    @Override
    @Transactional
    public String getUsername(UsernameRequestDto dto) {
        User user = userRepository.findByNameAndEmail(dto.getName(), dto.getEmail())
            .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));

        return user.getUsername();
    }

    //플랜에 사용자 초대 시 모든 사용자 목록을 return - 김민정
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
