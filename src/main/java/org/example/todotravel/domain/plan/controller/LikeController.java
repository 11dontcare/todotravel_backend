package org.example.todotravel.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.entity.Like;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.service.implement.LikeServiceImpl;
import org.example.todotravel.domain.plan.service.implement.PlanServiceImpl;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.impl.UserServiceImpl;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plan")
public class LikeController {
    private final LikeServiceImpl likeService;
    private final PlanServiceImpl planService;
    private final UserServiceImpl userService;

    //좋아요
    @PostMapping("/{plan_id}/like/{user_id}")
    public ApiResponse<Long> likePlan(@PathVariable("plan_id") Long planId, @PathVariable("user_id") Long userId){
        Plan plan = planService.getPlan(planId);
        User user = userService.getUserByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Like like = likeService.createLike(plan, user);
        return new ApiResponse<>(true, "좋아요 성공", like.getLikeId());
    }

    //좋아요 취소
    @DeleteMapping("/{plan_id}/like/{user_id}")
    public ApiResponse<Like> likeCancel(@PathVariable("plan_id") Long planId, @PathVariable("user_id") Long userId){
        Plan plan = planService.getPlan(planId);
        User user = userService.getUserByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        likeService.removeLike(plan, user);
        return new ApiResponse<>(true, "좋아요 취소 성공");
    }

    @GetMapping("/{plan_id}/isLiked/{user_id}")
    public ApiResponse<Boolean> isPlanLiked(@PathVariable("plan_id") Long planId, @PathVariable("user_id") Long userId) {
        Plan plan = planService.getPlan(planId);
        User user = userService.getUserByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Boolean isLiked = likeService.isPlanLikedByUser(user, plan);
        return new ApiResponse<>(true, "좋아요 여부 조회 성공", isLiked);
    }
}
