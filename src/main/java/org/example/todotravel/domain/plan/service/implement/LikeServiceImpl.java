package org.example.todotravel.domain.plan.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.notification.dto.request.AlarmRequestDto;
import org.example.todotravel.domain.notification.service.implement.AlarmServiceImpl;
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
    private final AlarmServiceImpl alarmService; //알림 자동 생성

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

    @Override
    @Transactional(readOnly = true)
    public Long countLike(Plan plan) {
        return likeRepository.countByPlan(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isPlanLikedByUser(User user, Plan plan) {
        return likeRepository.findByLikeUserAndPlan(user, plan).isPresent();
    }
}
