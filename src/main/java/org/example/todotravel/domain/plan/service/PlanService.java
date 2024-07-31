package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.dto.request.PlanRequestDto;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.repository.PlanRepository;
import org.springframework.stereotype.Service;

@Service
public interface PlanService {
    Plan createPlan(PlanRequestDto planRequestDto);
    void deletePlan(Long planId);
    Plan getPlan(Long planId);
    void updatePlan(Long planId, PlanRequestDto dto);
}
