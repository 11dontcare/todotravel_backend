package org.example.todotravel.domain.user.service.impl;

import io.jsonwebtoken.Claims;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.user.dto.request.*;
import org.example.todotravel.domain.user.dto.response.*;
import org.example.todotravel.domain.user.entity.Role;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.repository.UserRepository;
import org.example.todotravel.domain.user.service.UserService;
import org.example.todotravel.global.aws.S3Service;
import org.example.todotravel.global.exception.DuplicateUserException;
import org.example.todotravel.global.exception.SocialUserPasswordResetException;
import org.example.todotravel.global.exception.UserNotFoundException;
import org.example.todotravel.global.jwt.util.JwtTokenizer;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtTokenizer jwtTokenizer;
    private final S3Service s3Service;

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
            .info("현재 작성된 소개글이 없습니다.")
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

        return userRepository.save(newOAuth2User);
    }

    // OAuth2 가입 유저 추가 정보 업데이트
    @Override
    @Transactional
    public User updateOAuth2UserAdditionalInfo(OAuth2AdditionalInfoRequestDto dto) {
        Claims claims = jwtTokenizer.parseAccessToken(dto.getToken());
        String email = claims.getSubject();
        User user = getUserByEmail(email);

        user.setNickname(dto.getNickname());
        user.setGender(dto.getGender());
        user.setBirthDate(dto.getBirthDate());
        user.setRole(Role.ROLE_USER);

        return userRepository.save(user);
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

    // 소개글 업데이트
    @Override
    @Transactional
    public void updateUserInfo(User user, UserInfoRequestDto dto) {
        user.setInfo(dto.getNewInfo());
        userRepository.save(user);
    }

    // 비밀번호 재설정
    @Override
    @Transactional
    public void renewPassword(PasswordResetRequestDto dto, PasswordEncoder passwordEncoder) {
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    // 비밀번호 재설정 - 기존 비밀번호 검사도 수행
    @Override
    @Transactional
    public void updatePassword(User user, PasswordUpdateRequestDto dto, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(dto.getExistingPassword(), user.getPassword())) {
            throw new BadCredentialsException("기존 비밀번호가 일치하지 않습니다.");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    // 이름, 생년월일, 이메일로 사용자 찾기
    @Override
    @Transactional(readOnly = true)
    public PasswordSearchResponseDto findUserByNameAndBirthAndEmail(PasswordSearchRequestDto dto) {
        User user = userRepository.findByNameAndBirthDateAndEmail(dto.getName(), dto.getBirthDate(), dto.getEmail())
            .orElseThrow(() -> new UserNotFoundException("입력하신 정보와 일치하는 회원이 없어 인증번호를 발송할 수 없습니다."));

        // 소셜 로그인 사용자인 경우 예외 처리
        if (user.getSocialType() != null) {
            throw new SocialUserPasswordResetException("소셜 로그인 사용자는 비밀번호를 재설정할 수 없습니다.");
        }

        return new PasswordSearchResponseDto(user.getUserId());
    }

    // 이름, 이메일로 아이디 찾기
    @Override
    @Transactional(readOnly = true)
    public Object getUsernameOrEmail(UsernameRequestDto dto) {
        User user = userRepository.findByNameAndEmail(dto.getName(), dto.getEmail())
            .orElseThrow(() -> new UserNotFoundException("입력하신 정보와 일치하는 회원이 없어 인증번호를 발송할 수 없습니다."));

        if (user.getSocialType() != null) {
            return OAuth2EmailResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .createdDate(user.getCreatedDate())
                .socialType(user.getSocialType())
                .build();
        } else {
            return UsernameResponseDto.builder()
                .username(user.getUsername())
                .name(user.getName())
                .createdDate(user.getCreatedDate())
                .build();
        }
    }

    // 이메일로 사용자 찾기
    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
    }

    // userId로 사용자 찾기
    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
    }

    // 이메일로 userId 찾기
    @Override
    @Transactional(readOnly = true)
    public OAuth2SignUpResponseDto getUserIdByEmail(String email) {
        User user = userRepository.findUserIdByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(email + "::유저를 찾을 수 없습니다."));

        return OAuth2SignUpResponseDto.builder()
            .userId(user.getUserId())
            .build();
    }

    // 닉네임으로 사용자 찾기
    @Override
    @Transactional(readOnly = true)
    public User getUserIdByNickname(String nickname) {
        User user = userRepository.findByNickname(nickname)
            .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
        return user;
    }

    // 닉네임 업데이트
    @Override
    @Transactional
    public void updateNickname(User user, String newNickname) {
        // 닉네임 중복 검사
        checkDuplicateNickname(newNickname);

        // 중복되지 않는 닉네임이면 업데이트
        user.setNickname(newNickname);
        userRepository.save(user);
    }

    //플랜에 사용자 초대 시 모든 사용자 목록을 return - 김민정
    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
    }

    // 사용자 프로필 이미지 설정
    @Override
    public void updateProfileImage(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));

        String existingImageUrl = user.getProfileImageUrl();

        if (existingImageUrl != null && !existingImageUrl.isEmpty()) {
            String existingFileName = existingImageUrl.substring(existingImageUrl.lastIndexOf("/") + 1);
            try {
                s3Service.deleteFile(existingFileName);
            } catch (IOException e) {
                throw new RuntimeException("존재하는 프로필 이미지 삭제를 실패했습니다.", e);
            }
        }

        String imageUrl = null;
        try {
            imageUrl = s3Service.uploadFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);
    }

    @Override
    public User getProfileImageUrl(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));

    }

    // 회원 탈퇴 시 사용자 정보 모두 삭제
    @Override
    @Transactional
    public void removeUser(User user) {
        String existingImageUrl = user.getProfileImageUrl();

        if (existingImageUrl != null && !existingImageUrl.isEmpty()) {
            String existingFileName = existingImageUrl.substring(existingImageUrl.lastIndexOf("/") + 1);
            try {
                s3Service.deleteFile(existingFileName);
            } catch (IOException e) {
                throw new RuntimeException("존재하는 프로필 이미지 삭제를 실패했습니다.", e);
            }
        }

        // S3에서 프로필 이미지 삭제 후 사용자 삭제
        userRepository.deleteByUserId(user.getUserId());
    }
}
