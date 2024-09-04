package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.dto.request.VoteRequestDto;
import org.example.todotravel.domain.plan.dto.response.VoteResponseDto;
import org.example.todotravel.domain.plan.entity.Vote;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VoteService {
    Vote createVote(Long planId, User user, VoteRequestDto dto);
    Vote updateVote(Long planId, Long voteId, User user, VoteRequestDto dto) ;
    void removeVote(Long planId, Long voteId, User user);
    Page<VoteResponseDto> getAllVoteList(Long planId, Pageable pageable);
    Vote getVote(Long voteId);
}
