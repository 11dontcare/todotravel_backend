package org.example.todotravel.domain.user.service.impl;

import org.example.todotravel.domain.user.dto.request.OAuth2AdditionalInfoRequestDto;
import org.example.todotravel.domain.user.dto.request.OAuth2UserLoginRequestDto;
import org.example.todotravel.domain.user.dto.request.UserRegisterRequestDto;
import org.example.todotravel.domain.user.dto.request.UsernameRequestDto;
import org.example.todotravel.domain.user.entity.Gender;
import org.example.todotravel.domain.user.entity.Role;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.repository.UserRepository;
import org.example.todotravel.global.exception.DuplicateUserException;
import org.example.todotravel.global.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

// JUnit 5에서 Mockito를 사용하기 위한 확장 설정
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    // @Mock: 모의 객체 생성
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    // @InjectMocks: 모의 객체들을 주입
    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserRegisterRequestDto registerDto;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
            .userId(1L)
            .username("testuser")
            .password("encodedPassword")
            .nickname("testnick")
            .name("Test Name")
            .email("test@example.com")
            .role(Role.ROLE_USER)
            .build();

        registerDto = UserRegisterRequestDto.builder()
            .username("newuser")
            .password("password")
            .nickname("newnick")
            .name("New User")
            .email("newuser@example.com")
            .birthDate(LocalDate.of(1990, 1, 1))
            .build();
    }

    @Test
    @DisplayName("새 사용자 등록 테스트")
    void registerNewUser() {
        // given
        // when() : 모의 객체의 특정 메서드 호출에 대한 동작을 정의
        // anyString() : 어떤 문자열 인자가 들어와도 적용되도록 함
        // thenReturn() : 모의 객체 메서드가 반환할 값을 지정
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByNickname(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // when
        User result = userService.registerNewUser(registerDto, passwordEncoder);

        // then
        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
        // verify() : 모의 객체의 특정 메서드가 특정 인자로 몇 번 호출되는지 검증
        // times() : 호출 횟수를 지정
        // any() : 어떤 타입의 인자가 들어와도 적용되도록 함
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("OAuth2 사용자 로그인 테스트")
    void loginOAuth2User() {
        // given
        OAuth2UserLoginRequestDto dto = OAuth2UserLoginRequestDto.builder()
            .gender(Gender.MAN)
            .birthDate(LocalDate.of(1990, 1, 1))
            .build();

        User newOAuth2User = User.builder()
            .gender(Gender.MAN)
            .birthDate(LocalDate.of(1990, 1, 1))
            .role(Role.ROLE_USER)
            .build();

        when(userRepository.save(any(User.class))).thenReturn(newOAuth2User);

        // when
        User result = userService.loginOAuth2User(dto);

        // then
        assertNotNull(result);
        assertEquals(Gender.MAN, result.getGender());
        assertEquals(LocalDate.of(1990, 1, 1), result.getBirthDate());
        assertEquals(Role.ROLE_USER, result.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("OAuth2 사용자 추가 정보 업데이트 테스트")
    void updateOAuth2UserAdditionalInfo() {
        // given
        OAuth2AdditionalInfoRequestDto dto = OAuth2AdditionalInfoRequestDto.builder()
//            .userId(1L)
            .gender(Gender.MAN)
            .birthDate(LocalDate.of(1990, 1, 1))
            .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // when
        User result = userService.updateOAuth2UserAdditionalInfo(dto);

        // then
        assertNotNull(result);
        assertEquals(Gender.MAN, result.getGender());
        assertEquals(LocalDate.of(1990, 1, 1), result.getBirthDate());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("중복 사용자명 체크 테스트")
    void checkDuplicateUsername() {
        // given
        when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(testUser));

        // when & then
        assertThrows(DuplicateUserException.class, () -> userService.checkDuplicateUsername("existinguser"));
    }

    @Test
    @DisplayName("로그인 가능 체크 테스트")
    void checkLoginAvailable() {
        // given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("correctPassword", testUser.getPassword())).thenReturn(true);

        // when
        User result = userService.checkLoginAvailable("testuser", "correctPassword", passwordEncoder);

        // then
        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 잘못된 비밀번호")
    void checkLoginAvailable_WrongPassword() {
        // given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword", testUser.getPassword())).thenReturn(false);

        // when & then
        assertThrows(BadCredentialsException.class,
            () -> userService.checkLoginAvailable("testuser", "wrongPassword", passwordEncoder));
    }

//    @Test
//    @DisplayName("임시 비밀번호 설정 테스트")
//    void setTempPassword() {
//        // given
//        String email = "test@example.com";
//        String tempPassword = "tempPass123";
//        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
//        when(passwordEncoder.encode(tempPassword)).thenReturn("encodedTempPassword");
//
//        // when
//        userService.setTempPassword(email, tempPassword, passwordEncoder);
//
//        // then
//        verify(userRepository, times(1)).save(testUser);
//        assertEquals("encodedTempPassword", testUser.getPassword());
//    }

    @Test
    @DisplayName("이름과 이메일로 사용자명 찾기 테스트")
    void getUsername() {
        // given
        UsernameRequestDto dto = new UsernameRequestDto("Test Name", "test@example.com");
        when(userRepository.findByNameAndEmail(dto.getName(), dto.getEmail()))
            .thenReturn(Optional.of(testUser));

        // when
        String result = userService.getUsername(dto);

        // then
        assertEquals(testUser.getUsername(), result);
    }

    @Test
    @DisplayName("이메일로 사용자 찾기 테스트")
    void getUserByEmail() {
        // given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // when
        User result = userService.getUserByEmail("test@example.com");

        // then
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 사용자 찾기 테스트")
    void getUserByEmail_UserNotFound() {
        // given
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("nonexistent@example.com"));
    }

    @Test
    @DisplayName("모든 사용자 목록 가져오기 테스트")
    void getAllUsers() {
        // given
        List<User> userList = List.of(testUser, User.builder().userId(2L).username("user2").build());
        when(userRepository.findAll()).thenReturn(userList);

        // when
        List<User> result = userService.getAllUsers();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testUser.getUserId(), result.get(0).getUserId());
    }
}