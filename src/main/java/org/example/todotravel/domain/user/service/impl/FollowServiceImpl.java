package org.example.todotravel.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.user.dto.request.FollowRequestDto;
import org.example.todotravel.domain.user.dto.response.FollowResponseDto;
import org.example.todotravel.domain.user.entity.Follow;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.repository.FollowRepository;
import org.example.todotravel.domain.user.service.FollowService;
import org.example.todotravel.domain.user.service.UserService;
import org.example.todotravel.global.dto.PagedResponseDto;
import org.example.todotravel.global.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;

    /**
     * 팔로우 중인지 확인하는 메서드
     *
     * @param authentication 타인 페이지에 접근하는 사용자
     * @param followingUser  마이페이지 주인
     * @return boolean
     */
    @Override
    @Transactional(readOnly = true)
    public boolean checkFollowing(Authentication authentication, User followingUser) {
        if (authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return followRepository.existsByFollowerEmailAndFollowingEmail(userDetails.getEmail(), followingUser.getEmail());
        }
        return false;
    }

    // 팔로우
    @Override
    @Transactional
    public void startFollowing(FollowRequestDto dto) {
        User followerUser = userService.getUserById(dto.getFollowerId());
        User followingUser = userService.getUserById(dto.getFollowingId());
        if (followRepository.existsByFollowerUserAndFollowingUser(followerUser, followingUser)) {
            throw new RuntimeException("이미 팔로잉 중인 사용자입니다.");
        }

        Follow follow = new Follow(followerUser, followingUser);
        followRepository.save(follow);
    }

    // 팔로우 취소
    @Override
    @Transactional
    public void stopFollowing(FollowRequestDto dto) {
        User followerUser = userService.getUserById(dto.getFollowerId());
        User followingUser = userService.getUserById(dto.getFollowingId());
        Follow follow = followRepository.findByFollowerUserAndFollowingUser(followerUser, followingUser)
            .orElseThrow(() -> new RuntimeException("팔로잉 중인 사용자가 아닙니다."));

        followRepository.delete(follow);
    }

    // 팔로워 조회
    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<FollowResponseDto> getFollower(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> followers = followRepository.findFollowersByUserId(userId, pageable);

        Page<FollowResponseDto> followerDtos = followers.map(
            user -> new FollowResponseDto(user.getUserId(), user.getNickname())
        );

        return new PagedResponseDto<>(followerDtos);
    }

    // 팔로잉 조회
    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<FollowResponseDto> getFollowing(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> followings = followRepository.findFollowingsByUserId(userId, pageable);

        Page<FollowResponseDto> followingDtos = followings.map(
            user -> new FollowResponseDto(user.getUserId(), user.getNickname())
        );

        return new PagedResponseDto<>(followingDtos);
    }
}
