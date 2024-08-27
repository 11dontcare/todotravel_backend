package org.example.todotravel.domain.plan.service.implement;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.notification.dto.request.AlarmRequestDto;
import org.example.todotravel.domain.notification.service.AlarmService;
import org.example.todotravel.domain.plan.dto.request.PlanRequestDto;
import org.example.todotravel.domain.plan.dto.response.CommentResponseDto;
import org.example.todotravel.domain.plan.dto.response.PlanListResponseDto;
import org.example.todotravel.domain.plan.dto.response.PlanResponseDto;
import org.example.todotravel.domain.plan.dto.response.PlanSummaryDto;
import org.example.todotravel.domain.plan.entity.*;
import org.example.todotravel.domain.plan.repository.PlanRepository;
import org.example.todotravel.domain.plan.service.BookmarkService;
import org.example.todotravel.domain.plan.service.CommentService;
import org.example.todotravel.domain.plan.service.LikeService;
import org.example.todotravel.domain.plan.service.PlanService;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.repository.UserRepository;
import org.example.todotravel.global.aws.S3Service;
import org.example.todotravel.global.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;
    private final BookmarkService bookmarkService;
    private final LikeService likeService;
    private final AlarmService alarmService; //알림 자동 생성
    private final CommentService commentService;
    private final S3Service s3Service; // 버킷

    @Override
    @Transactional
    public Plan createPlan(PlanRequestDto planRequestDto, User user) {
        //플랜 생성 시 일정과 메모가 빈 플랜이 db에 생성

        Plan plan = planRequestDto.toEntity();
        plan.setPlanUser(user);
        //planUsers에 플랜 생성자 추가
        PlanUser planUser = PlanUser.builder()
            .status(PlanUser.StatusType.ACCEPTED)
            .user(user)
            .plan(plan)
            .build();
        plan.setPlanUsers(Collections.singleton(planUser));
        return planRepository.save(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public Plan getPlan(Long planId) {
        return planRepository.findByPlanId(planId).orElseThrow(() -> new RuntimeException("여행 플랜을 찾을 수 없습니다."));
    }

    @Override
    @Transactional
    public Plan updatePlan(Long planId, PlanRequestDto dto) {
        Plan plan = planRepository.findByPlanId(planId).orElseThrow(() -> new RuntimeException("여행 플랜을 찾을 수 없습니다."));

        //수정을 위해 toBuilder 사용
        plan = plan.toBuilder()
            .title(dto.getTitle())
            .location(dto.getLocation())
            .startDate(dto.getStartDate())
            .endDate(dto.getEndDate())
            .isPublic(dto.getIsPublic())
            .totalBudget(dto.getTotalBudget())
            .build();

        Plan updatedPlan = planRepository.save(plan);

        AlarmRequestDto requestDto = new AlarmRequestDto(plan.getPlanUser().getUserId(),
            "[" + plan.getTitle() + "] 플랜이 수정되었습니다.");
        alarmService.createAlarm(requestDto);

        return updatedPlan;
    }

    @Override
    @Transactional
    public void deletePlan(Plan plan) {
        commentService.removeAllByPlan(plan);
        bookmarkService.removeAllByPlan(plan);
        likeService.removeAllByPlan(plan);
        planRepository.deleteByPlanId(plan.getPlanId());
    }

    // 회원 탈퇴 시 사용자가 생성한 모든 플랜 삭제
    @Override
    @Transactional
    public void removeAllPlanByUser(User user) {
        planRepository.deleteAllByPlanUser(user);
    }

    // 회원 탈퇴 시 사용자가 생성한 해당 플랜에 대해서만 삭제
    @Override
    @Transactional
    public void removeJustPlan(Plan plan) {
        planRepository.deleteByPlanId(plan.getPlanId());
    }

    @Override
    @Transactional
    public List<PlanListResponseDto> getPublicPlans() {
        List<Plan> plans = planRepository.findAllByIsPublicTrue();
        List<PlanListResponseDto> planList = new ArrayList<>();
        for (Plan plan : plans) {
            planList.add(PlanListResponseDto.builder()
                .planId(plan.getPlanId())
                .title(plan.getTitle())
                .location(plan.getLocation())
                .description(plan.getDescription())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .planThumbnailUrl(plan.getPlanThumbnailUrl())
                .bookmarkNumber(bookmarkService.countBookmark(plan))
                .likeNumber(likeService.countLike(plan))
                .planUserNickname(plan.getPlanUser().getNickname())
                .build());
        }
        return planList;
    }

    @Override
    @Transactional
    public PlanResponseDto getPlanDetails(Long planId) {
        Plan plan = planRepository.findByPlanId(planId).orElseThrow(() -> new RuntimeException("플랜을 찾을 수 없습니다."));
        PlanResponseDto planResponseDto = PlanResponseDto.fromEntity(plan);
        List<Comment> comments = commentService.getCommentsByPlan(plan);
        List<CommentResponseDto> commentList = new ArrayList<>();
        for (Comment comment : comments) {
            commentList.add(CommentResponseDto.fromEntity(comment));
        }
        return planResponseDto.toBuilder()
            .commentList(commentList)
            .bookmarkNumber(bookmarkService.countBookmark(plan))
            .likeNumber(likeService.countLike(plan))
            .build();
    }

    @Override
    @Transactional
    public Plan copyPlan(Long planId, User user) {
        Plan plan = planRepository.findByPlanId(planId).orElseThrow(() -> new RuntimeException("플랜을 찾을 수 없습니다."));
        Plan newPlan = Plan.builder()
            .title(plan.getTitle())
            .location(plan.getLocation())
            .description(plan.getDescription())
            .startDate(plan.getStartDate())
            .endDate(plan.getEndDate())
            .isPublic(false)
            .status(false)
            .totalBudget(plan.getTotalBudget())
            .planUser(user)
            .build();
        List<Schedule> newSchedules = new ArrayList<>();
        for (Schedule schedule : plan.getSchedules()) {
            newSchedules.add(Schedule.builder()
                .status(false)
                .travelDayCount(schedule.getTravelDayCount())
                .description(schedule.getDescription())
                .travelTime(schedule.getTravelTime())
                .plan(newPlan)
                .location(schedule.getLocation())
                .build()
            );
        }
        newPlan.setSchedules(newSchedules);
        //planUsers에 플랜 생성자 추가
        PlanUser planUser = PlanUser.builder()
            .status(PlanUser.StatusType.ACCEPTED)
            .user(user)
            .plan(newPlan)
            .build();
        newPlan.setPlanUsers(Collections.singleton(planUser));
        return planRepository.save(newPlan);
    }

    @Override
    @Transactional
    public List<PlanListResponseDto> getSpecificPlans(String keyword) {
        List<Plan> plans = planRepository.findAllByIsPublicTrueAndTitleContains(keyword);
        List<PlanListResponseDto> planList = new ArrayList<>();
        for (Plan plan : plans) {
            planList.add(PlanListResponseDto.builder()
                .planId(plan.getPlanId())
                .title(plan.getTitle())
                .location(plan.getLocation())
                .description(plan.getDescription())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .bookmarkNumber(bookmarkService.countBookmark(plan))
                .likeNumber(likeService.countLike(plan))
                .build());
        }
        return planList;
    }

    @Override
    @Transactional
    public PlanResponseDto getPlanForModify(Long planId) {
        Plan plan = planRepository.findByPlanId(planId).orElseThrow(() -> new RuntimeException("여행 플랜을 찾을 수 없습니다."));
        PlanResponseDto planResponseDto = PlanResponseDto.fromEntity(plan);
        return planResponseDto.toBuilder()
            .bookmarkNumber(bookmarkService.countBookmark(plan))
            .likeNumber(likeService.countLike(plan))
            .build();
    }

    // 특정 사용자가 최근 북마크한 플랜 4개 조회 후 Dto로 반환
    @Override
    @Transactional(readOnly = true)
    public List<PlanListResponseDto> getRecentBookmarkedPlans(User user) {
        List<PlanSummaryDto> plans = bookmarkService.getRecentBookmarkedPlansByUser(user.getUserId());
        return plans.stream()
            .map(this::convertSummaryToPlanListResponseDto)
            .collect(Collectors.toList());
    }

    // 특정 사용자가 북마크한 플랜 조회 후 Dto로 반환
    @Override
    @Transactional(readOnly = true)
    public List<PlanListResponseDto> getAllBookmarkedPlans(User user) {
        List<Plan> plans = bookmarkService.getAllBookmarkedPlansByUser(user.getUserId());
        return plans.stream()
            .map(this::convertToPlanListResponseDto)
            .collect(Collectors.toList());
    }

    // 특정 사용자가 최근 좋아요한 플랜 4개 조회 후 Dto로 반환
    @Override
    @Transactional(readOnly = true)
    public List<PlanListResponseDto> getRecentLikedPlans(User user) {
        List<PlanSummaryDto> plans = likeService.getRecentLikedPlansByUser(user.getUserId());
        return plans.stream()
            .map(this::convertSummaryToPlanListResponseDto)
            .collect(Collectors.toList());
    }

    // 특정 사용자가 좋아요한 플랜 조회 후 Dto로 반환
    @Override
    @Transactional(readOnly = true)
    public List<PlanListResponseDto> getAllLikedPlans(User user) {
        List<Plan> plans = likeService.getAllLikedPlansByUser(user.getUserId());
        return plans.stream()
            .map(this::convertToPlanListResponseDto)
            .collect(Collectors.toList());
    }

    @Override
    public PlanListResponseDto convertToPlanListResponseDto(Plan plan) {
        return PlanListResponseDto.builder()
            .planId(plan.getPlanId())
            .title(plan.getTitle())
            .location(plan.getLocation())
            .description(plan.getDescription())
            .startDate(plan.getStartDate())
            .endDate(plan.getEndDate())
            .bookmarkNumber(bookmarkService.countBookmark(plan))
            .likeNumber(likeService.countLike(plan))
            .planUserNickname(plan.getPlanUser().getNickname())
            .build();
    }

    private PlanListResponseDto convertSummaryToPlanListResponseDto(PlanSummaryDto summaryDto) {
        return PlanListResponseDto.builder()
            .planId(summaryDto.getPlanId())
            .title(summaryDto.getTitle())
            .location(summaryDto.getLocation())
            .description(summaryDto.getDescription())
            .startDate(summaryDto.getStartDate())
            .endDate(summaryDto.getEndDate())
            .bookmarkNumber(bookmarkService.countBookmarkByPlanId(summaryDto.getPlanId()))
            .likeNumber(likeService.countLikeByPlanId(summaryDto.getPlanId()))
            .planUserNickname(summaryDto.getPlanUserNickname())
            .build();
    }

    // 사용자가 생성한 플랜 조회
    @Override
    @Transactional(readOnly = true)
    public List<Plan> getAllPlanByPlanUser(User user) {
        return planRepository.findByPlanUser(user);
    }

    // 프로필 썸네일 등록
    public void updateThumbnailImage(Long planId, MultipartFile file) {
        Plan plan = planRepository.findByPlanId(planId).orElseThrow(() -> new RuntimeException("플랜을 찾을 수 없습니다."));

        String existingImageUrl = plan.getPlanThumbnailUrl();

        if (existingImageUrl != null && !existingImageUrl.isEmpty()) {
            String existingFileName = existingImageUrl.substring(existingImageUrl.lastIndexOf("/") + 1);
            try {
                s3Service.deleteFile(existingFileName);
            } catch (IOException e) {
                throw new RuntimeException("존재하는 플랜 썸네일 삭제를 실패했습니다.", e);
            }
        }

        String imageUrl = null;
        try {
            imageUrl = s3Service.uploadFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        plan.setPlanThumbnailUrl(imageUrl);
        planRepository.save(plan);
    }

    @Override
    public Plan getThumbnailImageUrl(Long planId) {
        return planRepository.findById(planId).orElseThrow(() -> new RuntimeException("플랜을 찾을 수 없습니다."));
    }
}
