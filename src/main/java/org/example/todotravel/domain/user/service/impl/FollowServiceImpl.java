package org.example.todotravel.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.user.dto.request.FollowRequestDto;
import org.example.todotravel.domain.user.dto.response.FollowResponseDto;
import org.example.todotravel.domain.user.entity.Follow;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.repository.FollowRepository;
import org.example.todotravel.domain.user.service.FollowService;
import org.example.todotravel.domain.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;

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

    // 팔로잉 조회
    @Override
    @Transactional(readOnly = true)
    public List<FollowResponseDto> getFollowing(Long userId) {
        List<User> followings = followRepository.findFollowingsByUserId(userId);

        return followings.stream()
            .map(user -> new FollowResponseDto(user.getUserId(), user.getNickname()))
            .collect(Collectors.toList());
    }

    // 팔로워 조회
    @Override
    @Transactional(readOnly = true)
    public List<FollowResponseDto> getFollower(Long userId) {
        List<User> followers = followRepository.findFollowersByUserId(userId);

        return followers.stream()
            .map(user -> new FollowResponseDto(user.getUserId(), user.getNickname()))
            .collect(Collectors.toList());
    }
}
