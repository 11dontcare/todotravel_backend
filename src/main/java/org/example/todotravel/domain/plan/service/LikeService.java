package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.dto.response.PlanListResponseDto;
import org.example.todotravel.domain.plan.dto.response.PlanSummaryDto;
import org.example.todotravel.domain.plan.entity.Like;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;

import java.util.List;

public interface LikeService {
    Like createLike(Plan plan, User user);
    void removeLike(Plan plan, User user);
    void removeAllLikeByPlan(Plan plan);
    void removeAllLikeByUser(User user);
    Long countLike(Plan plan);
    Long countLikeByPlanId(Long planId);
    Boolean isPlanLikedByUser(User user, Plan plan);
    List<Plan> getAllLikedPlansByUser(Long userId);
    List<PlanSummaryDto> getRecentLikedPlansByUser(Long userId);
    void removeAllByPlan(Plan plan);
}
