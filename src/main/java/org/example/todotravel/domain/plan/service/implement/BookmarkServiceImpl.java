package org.example.todotravel.domain.plan.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.entity.Bookmark;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.repository.BookmarkRepository;
import org.example.todotravel.domain.plan.service.BookmarkService;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.impl.UserServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final PlanServiceImpl planService;
    private final UserServiceImpl userService;

    @Override
    @Transactional
    public Bookmark createBookmark(Long planId, Long userId) {
        Plan plan = planService.getPlan(planId);
        User user = userService.getUserByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Bookmark bookmark = Bookmark.builder()
                .bookmarkUser(user)
                .plan(plan)
                .build();
        return bookmarkRepository.save(bookmark);
    }

    @Override
    @Transactional
    public void removeBookmark(Long planId, Long userId) {
        Plan plan = planService.getPlan(planId);
        User user = userService.getUserByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        bookmarkRepository.deleteByPlanAndBookmarkUser(plan, user);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countBookmark(Plan plan) {
        return bookmarkRepository.countByPlan(plan);
    }
}
