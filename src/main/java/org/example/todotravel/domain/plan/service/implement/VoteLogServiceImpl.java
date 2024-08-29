package org.example.todotravel.domain.plan.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.entity.Vote;
import org.example.todotravel.domain.plan.entity.VoteLog;
import org.example.todotravel.domain.plan.repository.VoteLogRepository;
import org.example.todotravel.domain.plan.repository.VoteRepository;
import org.example.todotravel.domain.plan.service.LocationService;
import org.example.todotravel.domain.plan.service.PlanService;
import org.example.todotravel.domain.plan.service.VoteLogService;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.UserService;
import org.example.todotravel.global.jwt.util.JwtTokenizer;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteLogServiceImpl implements VoteLogService {
    private final VoteRepository voteRepository;
    private final VoteLogRepository voteLogRepository;
    private final PlanService planService;
    private final UserService userService;
    private final LocationService locationService;
    private final JwtTokenizer jwtTokenizer;


    // 투표하기
    @Override
    @Transactional
    public void castVote(Long voteId, String token) {
        Long userId = jwtTokenizer.getUserIdFromToken(token);
        User user = userService.getUserById(userId);

        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new RuntimeException("해당 투표를 찾을 수 없습니다."));

        Optional<VoteLog> voteLogOptional = voteLogRepository.findByVoteAndUser(vote, user);

        if (voteLogOptional.isPresent()) {
            VoteLog voteLog = voteLogOptional.get();
            if (voteLog.isVoted()) {
                vote.setVoteCount(vote.getVoteCount() - 1);
                voteLog.setVoted(false);
            } else {
                vote.setVoteCount(vote.getVoteCount() + 1);
                voteLog.setVoted(true);
            }
            voteLogRepository.save(voteLog);
        } else {
            vote.setVoteCount(vote.getVoteCount() + 1);
            VoteLog voteLog = VoteLog.builder()
                    .vote(vote)
                    .user(user)
                    .isVoted(true)
                    .build();
            voteLogRepository.save(voteLog);
        }

        voteRepository.save(vote);
    }
}