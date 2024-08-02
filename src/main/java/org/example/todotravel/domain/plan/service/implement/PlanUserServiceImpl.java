package org.example.todotravel.domain.plan.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.entity.PlanUser;
import org.example.todotravel.domain.plan.repository.PlanUserRepository;
import org.example.todotravel.domain.plan.service.PlanUserService;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.impl.UserServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanUserServiceImpl implements PlanUserService {
    private final PlanUserRepository planUserRepository;
    private final PlanServiceImpl planService;
    private final UserServiceImpl userService;

    //플랜 초대
    @Override
    @Transactional
    public PlanUser addPlanUser(Long planId, Long userId) {
        Plan plan = planService.getPlan(planId);
        User user = userService.getUserByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        PlanUser planUser = PlanUser.builder()
                .status(PlanUser.StatusType.PENDING)
                .user(user)
                .plan(plan)
                .build();
        return planUserRepository.save(planUser);
    }

    //플랜 초대 거절
    @Override
    @Transactional
    public PlanUser rejected(Long planParticipantId) {
        PlanUser planUser = planUserRepository.findById(planParticipantId).orElseThrow(() -> new RuntimeException("플랜 참여 상태를 찾을 수 없습니다."));
        planUser.setStatus(PlanUser.StatusType.REJECTED);
        return planUserRepository.save(planUser);
    }

    //플랜 초대 수락
    @Override
    @Transactional
    public PlanUser accepted(Long planParticipantId) {
        PlanUser planUser = planUserRepository.findById(planParticipantId).orElseThrow(() -> new RuntimeException("플랜 참여 상태를 찾을 수 없습니다."));
        planUser.setStatus(PlanUser.StatusType.ACCEPTED);
        return planUserRepository.save(planUser);
    }

    //플랜 참여자 조회
    @Override
    @Transactional(readOnly = true)
    public List<PlanUser> getAllPlanUser(Long planId) {
        Plan plan = planService.getPlan(planId);
        return planUserRepository.findAllByPlan(plan);
    }

    @Override
    @Transactional
    public PlanUser removePlanUser(Long planId, Long userId) {
        Plan plan = planService.getPlan(planId);
        User user = userService.getUserByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return planUserRepository.deletePlanUserByPlanAndUser(plan, user);
    }
}
