package org.example.todotravel.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.entity.Like;
import org.example.todotravel.domain.plan.service.implement.LikeServiceImpl;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plan")
public class LikeController {
    private final LikeServiceImpl likeService;

    //좋아요
    @PostMapping("/{plan_id}/like/{user_id}")
    public ApiResponse<Like> likePlan(@PathVariable("plan_id") Long planId, @PathVariable("user_id") Long userId){
        Like like = likeService.createLike(planId, userId);
        return new ApiResponse<>(true, "좋아요 성공", like);
    }

    //좋아요 취소
    @DeleteMapping("/{plan_id}/like/{user_id}")
    public ApiResponse<Like> likeCancel(@PathVariable("plan_id") Long planId, @PathVariable("user_id") Long userId){
        likeService.removeLike(planId, userId);
        return new ApiResponse<>(true, "좋아요 취소 성공");
    }
}
