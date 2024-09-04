package org.example.todotravel.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.service.VoteLogService;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.UserService;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
public class VoteLogController {
    private final VoteLogService voteLogService;
    private final UserService userService;

    // 투표하기 또는 투표 취소하기
    @PostMapping("/{vote_id}/vote")
    public ApiResponse<Void> castVote(@PathVariable(name = "vote_id") Long voteId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());

        voteLogService.castVote(voteId, user);
        return new ApiResponse<>(true, "투표가 처리되었습니다.", null);
    }
}