package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.dto.request.PlanRequestDto;
import org.example.todotravel.domain.plan.dto.response.PlanListResponseDto;
import org.example.todotravel.domain.plan.dto.response.PlanResponseDto;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;

import java.util.List;

public interface PlanService {
    Plan createPlan(PlanRequestDto planRequestDto, User user);
    void deletePlan(Long planId);
    Plan getPlan(Long planId);
    Plan updatePlan(Long planId, PlanRequestDto dto);
    List<PlanListResponseDto> getPublicPlans();

    PlanResponseDto getPlanDetails(Long planId);
    Plan copyPlan(Long planId);
    List<PlanListResponseDto> getSpecificPlans(String keyword);
    PlanResponseDto getPlanForModify(Long planId);

    List<PlanListResponseDto> getRecentBookmarkedPlans(Long userId);
    List<PlanListResponseDto> getAllBookmarkedPlans(Long userId);
    List<PlanListResponseDto> getRecentLikedPlans(Long userId);
    List<PlanListResponseDto> getAllLikedPlans(Long userId);
    PlanListResponseDto convertToPlanListResponseDto(Plan plan);
}
