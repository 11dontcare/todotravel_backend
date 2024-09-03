package org.example.todotravel.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.entity.Bookmark;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.service.BookmarkService;
import org.example.todotravel.domain.plan.service.PlanService;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.UserService;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plan")
public class BookmarkController {
    private final BookmarkService bookmarkService;
    private final UserService userService;
    private final PlanService planService;

    //북마크
    @PostMapping("/{plan_id}/bookmark/{user_id}")
    public ApiResponse<Long> bookmarkPlan(@PathVariable("plan_id") Long planId, @PathVariable("user_id") Long userId){
        Plan plan = planService.getPlan(planId);
        User user = userService.getUserByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Bookmark bookmark = bookmarkService.createBookmark(plan, user);
        return new ApiResponse<>(true, "북마크 성공", bookmark.getBookmarkId());
    }

    //북마크 취소
    @DeleteMapping("/{plan_id}/bookmark/{user_id}")
    public ApiResponse<Bookmark> bookmarkCancel(@PathVariable("plan_id") Long planId, @PathVariable("user_id") Long userId){
        Plan plan = planService.getPlan(planId);
        User user = userService.getUserByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        bookmarkService.removeBookmark(plan, user);
        return new ApiResponse<>(true, "북마크 취소 성공");
    }

    @GetMapping("/{plan_id}/isBookmarked/{user_id}")
    public ApiResponse<Boolean> isPlanBookmarked(@PathVariable("plan_id") Long planId, @PathVariable("user_id") Long userId) {
        Plan plan = planService.getPlan(planId);
        User user = userService.getUserByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Boolean isBookmarked = bookmarkService.isPlanBookmarkedByUser(user, plan);
        return new ApiResponse<>(true, "북마크 여부 조회 성공", isBookmarked);
    }
}
