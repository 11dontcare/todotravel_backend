package org.example.todotravel.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.dto.request.PlanRequestDto;
import org.example.todotravel.domain.plan.implement.PlanServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plan")
public class PlanController {
    private final PlanServiceImpl planServiceImpl;
    @PostMapping
    public ResponseEntity<String> createPlan(@RequestBody PlanRequestDto planRequestDto){
        planServiceImpl.createPlan(planRequestDto);
        return ResponseEntity.ok("플랜 생성 완료. 일정을 추가해주세요.");
    }
    @DeleteMapping("/{plan_id}")
    public ResponseEntity<String> deletePlan(@PathVariable("plan_id") Long planId){
        planServiceImpl.deletePlan(planId);
        return ResponseEntity.ok("플랜이 삭제되었습니다.");
    }

}
