package org.example.todotravel.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.service.VoteLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteLogController {
    private final VoteLogService voteLogService;

    // 투표하기 또는 투표 취소하기
    @PostMapping("/{vote_id}/vote")
    public ResponseEntity<String> castVote(@PathVariable(name = "vote_id") Long voteId, @RequestHeader("Authorization") String token) {
        voteLogService.castVote(voteId, token);
        return ResponseEntity.ok("투표가 처리되었습니다.");
    }
}