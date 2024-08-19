package org.example.todotravel.domain.user.service;

import org.example.todotravel.domain.user.dto.request.FollowRequestDto;
import org.example.todotravel.domain.user.dto.response.FollowResponseDto;

import java.util.List;

public interface FollowService {
    void startFollowing(FollowRequestDto dto);
    void stopFollowing(FollowRequestDto dto);
    List<FollowResponseDto> getFollowing(Long userId);
    List<FollowResponseDto> getFollower(Long userId);
}
