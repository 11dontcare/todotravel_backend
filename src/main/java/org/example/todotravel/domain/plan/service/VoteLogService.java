package org.example.todotravel.domain.plan.service;

public interface VoteLogService {
    void castVote(Long voteId, String token);
}
