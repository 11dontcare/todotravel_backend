package org.example.todotravel.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.dto.request.PlanRequestDto;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.implement.PlanServiceImpl;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plan")
public class PlanController {
    private final PlanServiceImpl planServiceImpl;
    @PostMapping
    public ApiResponse<Plan> createPlan(@RequestBody PlanRequestDto planRequestDto){
        Plan plan = planServiceImpl.createPlan(planRequestDto);
        return new ApiResponse<>(true, "플랜 생성 성공", plan);
    }
    @GetMapping("/{plan_id}")
    public void updatePlan(@PathVariable("plan_id") Long planId){
        Plan plan = planServiceImpl.getPlan(planId);
        //수정 창에 수정하려는 plan 정보
    }
    @PutMapping("/{plan_id}")
    public ApiResponse<Plan> updatePlan(@PathVariable("plan_id") Long planId, @RequestBody PlanRequestDto dto){
        planServiceImpl.updatePlan(planId, dto);
        return new ApiResponse<>(true, "플랜 수정 성공");
    }
    @DeleteMapping("/{plan_id}")
    public ApiResponse<Plan> deletePlan(@PathVariable("plan_id") Long planId){
        planServiceImpl.deletePlan(planId);
        return new ApiResponse<>(true, "플랜 삭제 성공");
    }

}
