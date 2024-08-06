package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.dto.request.PlanRequestDto;
import org.example.todotravel.domain.plan.dto.response.PlanListResponseDto;
import org.example.todotravel.domain.plan.dto.response.PlanResponseDto;
import org.example.todotravel.domain.plan.entity.Plan;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlanService {
    Plan createPlan(PlanRequestDto planRequestDto);
    void deletePlan(Long planId);
    Plan getPlan(Long planId);
    Plan updatePlan(Long planId, PlanRequestDto dto);
    List<PlanListResponseDto> getPublicPlans();
//    PlanResponseDto getPlanDetails(Long planId);
//    Plan copyPlan(Long planId);
}
