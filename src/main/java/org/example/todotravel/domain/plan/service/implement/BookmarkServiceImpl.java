package org.example.todotravel.domain.plan.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.notification.dto.request.AlarmRequestDto;
import org.example.todotravel.domain.notification.service.AlarmService;
import org.example.todotravel.domain.plan.dto.response.PlanSummaryDto;
import org.example.todotravel.domain.plan.entity.Bookmark;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.repository.BookmarkRepository;
import org.example.todotravel.domain.plan.service.BookmarkService;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final AlarmService alarmService; //알림 자동 생성

    @Override
    @Transactional
    public Bookmark createBookmark(Plan plan, User user) {
        Bookmark bookmark = Bookmark.builder()
                .bookmarkUser(user)
                .plan(plan)
                .build();
        Bookmark newBookmark = bookmarkRepository.save(bookmark);

        AlarmRequestDto requestDto = new AlarmRequestDto(plan.getPlanUser().getUserId(),
                user.getNickname()+ "님이 [" + plan.getTitle() + "] 플랜을 북마크 했습니다.");
        alarmService.createAlarm(requestDto);

        return newBookmark;
    }

    @Override
    @Transactional
    public void removeBookmark(Plan plan, User user) {
        bookmarkRepository.deleteByPlanAndBookmarkUser(plan, user);
    }

    // 회원 탈퇴 시 사용자가 생성한 플랜의 북마크 삭제
    @Override
    @Transactional
    public void removeAllBookmarksByPlan(Plan plan) {
        bookmarkRepository.deleteAllByPlan(plan);
    }

    // 회원 탈퇴 시 북마크 전체 삭제
    @Override
    @Transactional
    public void removeAllBookmarkByUser(User user) {
        bookmarkRepository.deleteAllByBookmarkUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countBookmark(Plan plan) {
        return bookmarkRepository.countByPlan(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countBookmarkByPlanId(Long planId) {
        return bookmarkRepository.countByPlanPlanId(planId);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isPlanBookmarkedByUser(User user, Plan plan) {
        return bookmarkRepository.findByBookmarkUserAndPlan(user, plan).isPresent();
    }

    // 특정 사용자가 북마크한 플랜 조회
    @Override
    @Transactional(readOnly = true)
    public List<Plan> getAllBookmarkedPlansByUser(Long userId) {
        return bookmarkRepository.findBookmarkedPlansByUserId(userId);
    }

    // 특정 사용자가 최근 북마크한 플랜 4개 조회
    @Override
    @Transactional(readOnly = true)
    public List<PlanSummaryDto> getRecentBookmarkedPlansByUser(Long userId) {
        return bookmarkRepository.findRecentCommentedPlansByUserId(userId);
    }

    //플랜 삭제 시 플랜에 달린 북마크 삭제
    @Override
    public void removeAllByPlan(Plan plan) {
        bookmarkRepository.deleteAllByPlan(plan);
    }
}
