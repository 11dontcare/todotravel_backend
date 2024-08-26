package org.example.todotravel.domain.plan.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.notification.dto.request.AlarmRequestDto;
import org.example.todotravel.domain.notification.service.AlarmService;
import org.example.todotravel.domain.plan.dto.response.PlanSummaryDto;
import org.example.todotravel.domain.plan.entity.Like;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.repository.LikeRepository;
import org.example.todotravel.domain.plan.service.LikeService;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final AlarmService alarmService; //알림 자동 생성

    @Override
    @Transactional
    public Like createLike(Plan plan, User user) {
        Like like = Like.builder()
                .likeUser(user)
                .plan(plan)
                .build();
        Like newLike = likeRepository.save(like);

        AlarmRequestDto requestDto = new AlarmRequestDto(plan.getPlanUser().getUserId(),
                user.getNickname()+ "님이 [" + plan.getTitle() + "] 플랜을 좋아합니다.");
        alarmService.createAlarm(requestDto);

        return newLike;
    }

    @Override
    @Transactional
    public void removeLike(Plan plan, User user) {
        likeRepository.deleteByPlanAndLikeUser(plan, user);
    }

    // 회원 탈퇴 시 사용자가 생성한 플랜의 좋아요 삭제
    @Override
    @Transactional
    public void removeAllLikeByPlan(Plan plan) {
        likeRepository.deleteAllByPlan(plan);
    }

    // 회원 탈퇴 시 좋아요 전체 삭제
    @Override
    @Transactional
    public void removeAllLikeByUser(User user) {
        likeRepository.deleteAllByLikeUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countLike(Plan plan) {
        return likeRepository.countByPlan(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countLikeByPlanId(Long planId) {
        return likeRepository.countByPlanPlanId(planId);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isPlanLikedByUser(User user, Plan plan) {
        return likeRepository.findByLikeUserAndPlan(user, plan).isPresent();
    }

    // 특정 사용자가 좋아요한 플랜 조회
    @Override
    @Transactional(readOnly = true)
    public List<Plan> getAllLikedPlansByUser(Long userId) {
        return likeRepository.findLikedPlansByUserId(userId);
    }

    // 특정 사용자가 최근 좋아요한 플랜 3개 조회
    @Override
    @Transactional(readOnly = true)
    public List<PlanSummaryDto> getRecentLikedPlansByUser(Long userId) {
        return likeRepository.findRecentLikedPlansByUserId(userId);
    }

    //플랜 삭제 시 플랜에 달린 좋아요 삭제
    @Override
    public void removeAllByPlan(Plan plan) {
        likeRepository.deleteAllByPlan(plan);
    }
}
