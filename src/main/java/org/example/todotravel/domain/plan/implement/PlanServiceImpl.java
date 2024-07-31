package org.example.todotravel.domain.plan.implement;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.dto.request.PlanRequestDto;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.entity.PlanUser;
import org.example.todotravel.domain.plan.entity.Schedule;
import org.example.todotravel.domain.plan.repository.PlanRepository;
import org.example.todotravel.domain.plan.service.PlanService;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;

    @Override
    public Plan createPlan(PlanRequestDto planRequestDto) {
        //플랜 생성 시 일정과 메모가 빈 플랜이 db에 생성

        Plan plan = planRequestDto.toEntity();
        //현재 로그인 중인 사용자 user
        User user = new User();
        plan.setPlanUser(user);
        //planUsers에 플랜 생성자 추가, 나중엔 user에서 정보 받아오도록
        PlanUser planUser = new PlanUser();
        plan.setPlanUsers(Collections.singleton(planUser));
        return planRepository.save(plan);
    }

    @Override
    public Plan getPlan(Long planId) {
        return planRepository.findByPlanId(planId);
    }

    @Override
    public void updatePlan(Long planId, PlanRequestDto dto) {
        Plan plan = planRepository.findByPlanId(planId);
        plan.setTitle(dto.getTitle());
        plan.setLocation(dto.getLocation());
        plan.setStartDate(dto.getStartDate());
        plan.setEndDate(dto.getEndDate());
        plan.setIsPublic(dto.getIsPublic());
        plan.setTotalBudget(dto.getTotalBudget());
        planRepository.save(plan);
    }

    @Override
    public void deletePlan(Long planId) {
        planRepository.deleteByPlanId(planId);
    }
}
