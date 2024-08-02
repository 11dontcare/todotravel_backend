package org.example.todotravel.domain.plan.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.dto.request.PlanRequestDto;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.entity.PlanUser;
import org.example.todotravel.domain.plan.service.implement.PlanServiceImpl;
import org.example.todotravel.domain.plan.service.implement.PlanUserServiceImpl;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.impl.UserServiceImpl;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plan")
public class PlanController {
    private final PlanServiceImpl planService;
    private final UserServiceImpl userService;
    private final PlanUserServiceImpl planUserService;
    //플랜 생성
    @PostMapping
    public ApiResponse<Plan> createPlan(@Valid @RequestBody PlanRequestDto planRequestDto){
        Plan plan = planService.createPlan(planRequestDto);
        return new ApiResponse<>(true, "플랜 생성 성공", plan);
    }
    //플랜 수정 창
    @GetMapping("/{plan_id}")
    public ApiResponse<Plan> updatePlan(@PathVariable("plan_id") Long planId){
        Plan plan = planService.getPlan(planId);
        //수정 창에 수정하려는 plan 정보
        return new ApiResponse<>(true, "수정할 플랜 조회 성공", plan);
    }
    //플랜 수정
    @PutMapping("/{plan_id}")
    public ApiResponse<Plan> updatePlan(@PathVariable("plan_id") Long planId, @RequestBody PlanRequestDto dto){
        planService.updatePlan(planId, dto);
        return new ApiResponse<>(true, "플랜 수정 성공");
    }
    //플랜 삭제
    @DeleteMapping("/{plan_id}")
    public ApiResponse<Plan> deletePlan(@PathVariable("plan_id") Long planId){
        planService.deletePlan(planId);
        return new ApiResponse<>(true, "플랜 삭제 성공");
    }
    //플랜 사용자 초대(초대할 사용자 조회)
    @GetMapping("/{plan_id}/invite")
    public ApiResponse<List<User>> invite(@PathVariable("plan_id") Long planId){
        List<User> users = userService.getAllUsers();
        Plan plan = planService.getPlan(planId);
        //해당 플랜에 참여하고 있지 않은 사용자만
        users.forEach(user -> {
            if(user.getPlans().contains(plan))
                users.remove(user);
        });
        return new ApiResponse<>(true, "사용자 목록 조회 성공", users);
    }
    //플랜 사용자 초대
    @PostMapping("/{plan_id}/invite/{user_id}")
    public ApiResponse<PlanUser> inviteUser(@PathVariable("plan_id") Long planId, @PathVariable("user_id") Long userId){
        PlanUser planUser = planUserService.addPlanUser(planId, userId);
        return new ApiResponse<>(true, "사용자 초대 성공", planUser);
    }
}
