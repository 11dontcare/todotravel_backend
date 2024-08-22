package org.example.todotravel.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import org.example.todotravel.domain.notification.entity.Alarm;
import org.example.todotravel.domain.plan.entity.Plan;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // ID
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    // 이메일
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    // 비밀번호
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    // 이름
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    // 닉네임
    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    // 성별
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    // 가입 날짜
    @Column(name = "created_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDate;

    // 생년월일
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "info", length = 160)
    private String info;

    // 권한설정
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    // 소셜 타입, KAKAO, NAVER, GOOGLE
    @Column(name = "social_type")
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    // 로그인한 소셜 타입의 식별자 값, 일반인 경우 null
    @Column(name = "social_id", length = 255)
    private String socialId;

    // 여러 디바이스에서 접속한다고 가정하면 1:N
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RefreshToken> refreshTokens;

    @OneToMany(mappedBy = "followerUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> followers;

    @OneToMany(mappedBy = "followingUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> followings;

    @OneToMany(mappedBy = "planUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Plan> plans;

    @OneToMany(mappedBy = "alarmUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Alarm> alarms;
}
