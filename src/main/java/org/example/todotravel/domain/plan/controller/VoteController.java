package org.example.todotravel.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.dto.request.VoteRequestDto;
import org.example.todotravel.domain.plan.dto.response.VoteResponseDto;
import org.example.todotravel.domain.plan.entity.Vote;
import org.example.todotravel.domain.plan.service.VoteService;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.UserService;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vote")
public class VoteController {
    private final VoteService voteService;
    private final UserService userService;

    //투표 생성
    @PostMapping("/{plan_id}")
    public ApiResponse<VoteResponseDto> createVote(@PathVariable("plan_id") Long planId,
                                                   @RequestBody VoteRequestDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());

        Vote vote = voteService.createVote(planId, user, dto);
        VoteResponseDto responseDto = VoteResponseDto.fromEntity(vote);
        return new ApiResponse<>(true, "투표 생성 완료", responseDto);
    }

    //투표 수정 (종류일, 카테고리)
    @PutMapping("/{plan_id}/{vote_id}")
    public ApiResponse<VoteResponseDto> updateVote(@PathVariable("plan_id") Long planId,
                                                   @PathVariable("vote_id") Long voteId,
                                                   @RequestBody VoteRequestDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());

        Vote vote = voteService.updateVote(planId, voteId, user, dto);
        VoteResponseDto responseDto = VoteResponseDto.fromEntity(vote);
        return new ApiResponse<>(true, "투표 수정 완료", responseDto);
    }

    //투표 삭제
    @DeleteMapping("/{vote_id}")
    public ApiResponse<Void> deleteVote(@PathVariable("vote_id") Long voteId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());

        voteService.deleteVote(voteId, user);
        return new ApiResponse<>(true, "투표를 삭제했습니다.", null);
    }

    //투표 전체 보기
    @GetMapping("/{plan_id}/list")
    public ApiResponse<Page<VoteResponseDto>> showAllVotes(@PathVariable("plan_id") Long planId,
                                                           Pageable pageable) {
        Page<VoteResponseDto> voteList = voteService.getAllVoteList(planId, pageable);
        return new ApiResponse<>(true, "투표 목록 조회 성공", voteList);
    }

    //투표 단일 보기
    @GetMapping("/{vote_id}")
    public ApiResponse<VoteResponseDto> showVote(@PathVariable("vote_id") Long voteId) {
        Vote vote = voteService.getVote(voteId);
        VoteResponseDto responseDto = VoteResponseDto.fromEntity(vote);
        return new ApiResponse<>(true, "투표 목록 단일 조회 성공", responseDto);
    }
}
