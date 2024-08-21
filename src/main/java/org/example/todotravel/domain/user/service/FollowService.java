package org.example.todotravel.domain.user.service;

import org.example.todotravel.domain.user.dto.request.FollowRequestDto;
import org.example.todotravel.domain.user.dto.response.FollowResponseDto;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.global.dto.PagedResponseDto;
import org.springframework.security.core.Authentication;

public interface FollowService {
    boolean checkFollowing(Authentication authentication, User followingUser);
    void startFollowing(FollowRequestDto dto);
    void stopFollowing(FollowRequestDto dto);
    PagedResponseDto<FollowResponseDto> getFollower(Long userId, int page, int size);
    PagedResponseDto<FollowResponseDto> getFollowing(Long userId, int page, int size);
}
