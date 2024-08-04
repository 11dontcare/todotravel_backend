package org.example.todotravel.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.entity.Bookmark;
import org.example.todotravel.domain.plan.service.implement.BookmarkServiceImpl;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plan")
public class BookmarkController {
    private final BookmarkServiceImpl bookmarkService;

    //북마크
    @PostMapping("/{plan_id}/bookmark/{user_id}")
    public ApiResponse<Bookmark> bookmarkPlan(@PathVariable("plan_id") Long planId, @PathVariable("user_id") Long userId){
        Bookmark bookmark = bookmarkService.createBookmark(planId, userId);
        return new ApiResponse<>(true, "북마크 성공", bookmark);
    }

    //북마크 취소
    @DeleteMapping("/{plan_id}/bookmark/{user_id}")
    public ApiResponse<Bookmark> bookmarkCancel(@PathVariable("plan_id") Long planId, @PathVariable("user_id") Long userId){
        bookmarkService.removeBookmark(planId, userId);
        return new ApiResponse<>(true, "북마크 취소 성공");
    }
}
