package org.example.todotravel.domain.plan.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.entity.Like;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.repository.LikeRepository;
import org.example.todotravel.domain.plan.service.LikeService;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.impl.UserServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final PlanServiceImpl planService;
    private final UserServiceImpl userService;

    @Override
    @Transactional
    public Like createLike(Long planId, Long userId) {
        Plan plan = planService.getPlan(planId);
        User user = userService.getUserByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Like like = Like.builder()
                .likeUser(user)
                .plan(plan)
                .build();
        return likeRepository.save(like);
    }

    @Override
    @Transactional
    public void removeLike(Long planId, Long userId) {
        Plan plan = planService.getPlan(planId);
        User user = userService.getUserByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        likeRepository.deleteByPlanAndLikeUser(plan, user);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countLike(Plan plan) {
        return likeRepository.countByPlan(plan);
    }
}
