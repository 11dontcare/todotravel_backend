package org.example.todotravel.domain.plan.repository;

import org.example.todotravel.domain.plan.entity.Vote;
import org.example.todotravel.domain.plan.entity.VoteLog;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteLogRepository extends JpaRepository<VoteLog, Long> {
    Optional<VoteLog> findByVoteAndUser(Vote vote, User user);
}