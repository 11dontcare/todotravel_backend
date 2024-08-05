package org.example.todotravel.domain.plan.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.entity.Like;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.repository.LikeRepository;
import org.example.todotravel.domain.plan.service.LikeService;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;

    @Override
    @Transactional
    public Like createLike(Plan plan, User user) {
        Like like = Like.builder()
                .likeUser(user)
                .plan(plan)
                .build();
        return likeRepository.save(like);
    }

    @Override
    @Transactional
    public void removeLike(Plan plan, User user) {
        likeRepository.deleteByPlanAndLikeUser(plan, user);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countLike(Plan plan) {
        return likeRepository.countByPlan(plan);
    }
}
