package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.user.entity.User;

public interface VoteLogService {
    void castVote(Long voteId, User user);
}
