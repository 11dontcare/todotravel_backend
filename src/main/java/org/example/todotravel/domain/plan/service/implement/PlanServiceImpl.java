package org.example.todotravel.domain.plan.service.implement;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.notification.dto.request.AlarmRequestDto;
import org.example.todotravel.domain.notification.service.AlarmService;
import org.example.todotravel.domain.plan.dto.request.PlanRequestDto;
import org.example.todotravel.domain.plan.dto.response.*;
import org.example.todotravel.domain.plan.entity.*;
import org.example.todotravel.domain.plan.repository.PlanRepository;
import org.example.todotravel.domain.plan.service.BookmarkService;
import org.example.todotravel.domain.plan.service.CommentService;
import org.example.todotravel.domain.plan.service.LikeService;
import org.example.todotravel.domain.plan.service.PlanService;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.repository.UserRepository;
import org.example.todotravel.global.aws.S3Service;
import org.example.todotravel.global.dto.PagedResponseDto;
import org.example.todotravel.global.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
    public Plan createPlan(PlanRequestDto planRequestDto, MultipartFile planThumbnail, User user) {


        String thumbnailUrl = null;
        if (planThumbnail != null && !planThumbnail.isEmpty()) {
            try {
                thumbnailUrl = s3Service.uploadFile(planThumbnail);
                planRequestDto.setPlanThumbnailUrl(thumbnailUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

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
        plan.setViewCount(0L);
        plan.setRecruitment(false);
        return planRepository.save(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public Plan getPlan(Long planId) {
        return planRepository.findByPlanId(planId).orElseThrow(() -> new RuntimeException("여행 플랜을 찾을 수 없습니다."));
    }

    @Override
    @Transactional
    public Plan updatePlan(Long planId, PlanRequestDto dto, MultipartFile newPlanThumbnail) {
        Plan plan = planRepository.findByPlanId(planId).orElseThrow(() -> new RuntimeException("여행 플랜을 찾을 수 없습니다."));

        // 새 썸네일이 존재할 경우, 존재하는 썸네일 이미지 S3에서 삭제
        String existingImageUrl = plan.getPlanThumbnailUrl();
        if (newPlanThumbnail != null && !newPlanThumbnail.isEmpty() &&
            existingImageUrl != null && !existingImageUrl.isEmpty()) {
            String existingFileName = existingImageUrl.substring(existingImageUrl.lastIndexOf("/") + 1);
            try {
                s3Service.deleteFile(existingFileName);
            } catch (IOException e) {
                throw new RuntimeException("존재하는 썸네일 이미지 삭제를 실패했습니다.", e);
            }
        }

        // 썸네일 업데이트 (S3 & DB)
        String imageUrl = null;
        if (newPlanThumbnail != null && !newPlanThumbnail.isEmpty()) {
            try {
                imageUrl = s3Service.uploadFile(newPlanThumbnail);
                plan.setPlanThumbnailUrl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("썸네일 업데이트에 실패했습니다.", e);
            }
        }

        // 수정을 위해 toBuilder 사용
        plan = plan.toBuilder()
            .title(dto.getTitle())
            .frontLocation(dto.getFrontLocation())
            .location(dto.getLocation())
            .startDate(dto.getStartDate())
            .endDate(dto.getEndDate())
            .isPublic(dto.getIsPublic())
            .totalBudget(dto.getTotalBudget())
            .build();

        Plan updatedPlan = planRepository.save(plan);
        alarmService.createAlarm(
            new AlarmRequestDto(plan.getPlanUser().getUserId(), "[" + plan.getTitle() + "] 플랜이 수정되었습니다.")
        );

        return updatedPlan;
    }

    @Override
    @Transactional
    public void removePlan(Plan plan) {
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
        // 해당 플랜에 썸네일이 존재할 경우 S3에서도 제거
        String existingImageUrl = plan.getPlanThumbnailUrl();
        if (existingImageUrl != null && !existingImageUrl.isEmpty()) {
            String existingFileName = existingImageUrl.substring(existingImageUrl.lastIndexOf("/") + 1);
            try {
                s3Service.deleteFile(existingFileName);
            } catch (IOException e) {
                throw new RuntimeException("존재하는 썸네일 이미지 삭제를 실패했습니다.", e);
            }
        }

        planRepository.deleteByPlanId(plan.getPlanId());
    }

    @Override
    @Transactional
    public List<PlanListResponseDto> getPublicPlans() {
        List<Plan> plans = planRepository.findAllByIsPublicTrue();
        return convertToPlanListResponseDto(plans);
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

        // 해당 플랜의 조회 수 증가 (인기순 정렬을 위한)
        planRepository.incrementViewCount(planId);

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
            .frontLocation(plan.getFrontLocation())
            .location(plan.getLocation())
            .description(plan.getDescription())
            .startDate(plan.getStartDate())
            .endDate(plan.getEndDate())
            .isPublic(false)
            .status(false)
            .recruitment(false)
            .totalBudget(plan.getTotalBudget())
            .viewCount(0L)
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
                .planUserNickname(plan.getPlanUser().getNickname())
                .planThumbnailUrl(plan.getPlanThumbnailUrl())
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
        List<PlanListResponseDto> dtos = bookmarkService.getRecentBookmarkedPlansByUser(user.getUserId());
        return setBookmarkAndLikeCounts(dtos);
    }

    // 특정 사용자가 북마크한 플랜 조회 후 Dto로 반환
    @Override
    @Transactional(readOnly = true)
    public List<PlanListResponseDto> getAllBookmarkedPlans(User user) {
        List<PlanListResponseDto> dtos = bookmarkService.getAllBookmarkedPlansByUser(user.getUserId());
        return setBookmarkAndLikeCounts(dtos);
    }

    // 특정 사용자가 최근 좋아요한 플랜 4개 조회 후 Dto로 반환
    @Override
    @Transactional(readOnly = true)
    public List<PlanListResponseDto> getRecentLikedPlans(User user) {
        List<PlanListResponseDto> dtos = likeService.getRecentLikedPlansByUser(user.getUserId());
        return setBookmarkAndLikeCounts(dtos);
    }

    // 특정 사용자가 좋아요한 플랜 조회 후 Dto로 반환
    @Override
    @Transactional(readOnly = true)
    public List<PlanListResponseDto> getAllLikedPlans(User user) {
        List<PlanListResponseDto> dtos = likeService.getAllLikedPlansByUser(user.getUserId());
        return setBookmarkAndLikeCounts(dtos);
    }

    // 공통 메서드: PlanListResponseDto 리스트에 북마크와 좋아요 수를 설정하는 메서드
    @Override
    public List<PlanListResponseDto> setBookmarkAndLikeCounts(List<PlanListResponseDto> dtos) {
        List<Long> planIds = dtos.stream().map(PlanListResponseDto::getPlanId).collect(Collectors.toList());
        Map<Long, PlanCountProjection> countMap = getBookmarkAndLikeCounts(planIds);

        dtos.forEach(dto -> {
            PlanCountProjection counts = countMap.get(dto.getPlanId());
            if (counts != null) {
                dto.setBookmarkNumber(counts.getBookmarkCount());
                dto.setLikeNumber(counts.getLikeCount());
            }
        });

        return dtos;
    }

    @Override
    public List<PlanListResponseDto> convertToPlanListResponseDto(List<Plan> plans) {
        List<Long> planIds = plans.stream().map(Plan::getPlanId).collect(Collectors.toList());
        Map<Long, PlanCountProjection> countMap = getBookmarkAndLikeCounts(planIds);

        return plans.stream().map(plan -> {
            PlanCountProjection counts = countMap.get(plan.getPlanId());
            return PlanListResponseDto.builder()
                .planId(plan.getPlanId())
                .title(plan.getTitle())
                .location(plan.getLocation())
                .description(plan.getDescription())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .bookmarkNumber(counts != null ? counts.getBookmarkCount() : 0)
                .likeNumber(counts != null ? counts.getLikeCount() : 0)
                .participantsCount(plan.getParticipantsCount())
                .planUserCount(plan.getPlanUsers().stream().filter(planUser -> planUser.getStatus() == PlanUser.StatusType.ACCEPTED).count())
                .planUserNickname(plan.getPlanUser().getNickname())
                .planThumbnailUrl(plan.getPlanThumbnailUrl())
                .build();
        }).collect(Collectors.toList());
    }

    // 공통 메서드: planId 리스트의 북마크 수와 좋아요 수를 카운팅하는 메서드
    @Override
    public Map<Long, PlanCountProjection> getBookmarkAndLikeCounts(List<Long> planIds) {
        List<PlanCountProjection> counts = planRepository.countBookmarksAndLikesByPlanIds(planIds);
        return counts.stream()
            .collect(Collectors.toMap(PlanCountProjection::getPlanId, Function.identity()));
    }

    // 모집 중이지 않은 플랜 중 인기순으로 페이징 조회
    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<PlanListResponseDto> getPopularPlansNotInRecruitment(int page, int size) {
        return getPagedPlans(page, size, planRepository::findPopularPlansNotInRecruitment);
    }

    // 모집 중이지 않은 플랜 중 행정구역과 인기순으로 페이징 조회
    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<PlanListResponseDto> getPopularPlansWithFrontLocation(int page, int size, String frontLocation) {
        return getPagedPlans(page, size, pageable -> planRepository.findPopularPlansWithFrontLocation(frontLocation, pageable));
    }

    // 모집 중이지 않은 플랜 중 행정구역+도시와 인기순으로 페이징 조회
    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<PlanListResponseDto> getPopularPlansWithAllLocation(int page, int size, String frontLocation, String location) {
        return getPagedPlans(page, size, pageable -> planRepository.findPopularPlansWithAllLocation(frontLocation, location, pageable));
    }

    // 플랜 중 최신순으로 페이징 조회
    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<PlanListResponseDto> getRecentPlansByRecruitment(int page, int size, Boolean recruitment) {
        return getPagedPlans(page, size, pageable -> planRepository.findRecentPlansByRecruitment(recruitment, pageable));
    }

    // 플랜 중 행정구역과 최신순으로 페이징 조회
    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<PlanListResponseDto> getRecentPlansWithFrontLocation(int page, int size, String frontLocation, Boolean recruitment) {
        return getPagedPlans(page, size, pageable -> planRepository.findRecentPlansWithFrontLocation(frontLocation, recruitment, pageable));
    }

    // 플랜 중 행정구역+도시와 최신순으로 페이징 조회
    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<PlanListResponseDto> getRecentPlansWithAllLocation(int page, int size, String frontLocation, String location, Boolean recruitment) {
        return getPagedPlans(page, size, pageable -> planRepository.findRecentPlansWithAllLocation(frontLocation, location, recruitment, pageable));
    }

    // 플랜 중 여행 시작 날짜와 최신순으로 페이징 조회
    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<PlanListResponseDto> getRecentPlansRecruitmentByStartDate(int page, int size, Boolean recruitment, LocalDate startDate) {
        return getPagedPlans(page, size, pageable -> planRepository.findRecentPlansRecruitmentByStartDate(recruitment, startDate, pageable));
    }

    // 플랜 중 행정구역, 여행 시작 날짜와 최신순으로 페이징 조회
    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<PlanListResponseDto> getRecentPlansWithFrontLocationAndStartDate(int page, int size, String frontLocation, Boolean recruitment, LocalDate startDate) {
        return getPagedPlans(page, size, pageable -> planRepository.findRecentPlansWithFrontLocationAndStartDate(frontLocation, recruitment, startDate, pageable));
    }

    // 플랜 중 행정구역+도시, 여행 시작 날짜와 최신순으로 페이징 조회
    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<PlanListResponseDto> getRecentPlansWithAllLocationAndStartDate(int page, int size, String frontLocation, String location, Boolean recruitment, LocalDate startDate) {
        return getPagedPlans(page, size, pageable -> planRepository.findRecentPlansWithAllLocationAndStartDate(frontLocation, location, recruitment, startDate, pageable));
    }

    // 페이징 플랜 공통 메서드
    private PagedResponseDto<PlanListResponseDto> getPagedPlans(int page, int size, Function<Pageable, Page<Plan>> queryFunction) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Plan> plans = queryFunction.apply(pageable);
        List<PlanListResponseDto> planListDtos = convertToPlanListResponseDto(plans.getContent());
        return new PagedResponseDto<>(new PageImpl<>(planListDtos));
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

    @Override
    public void savePlan(Plan plan) {
        planRepository.save(plan);
    }

    //플랜 모집
    @Override
    @Transactional
    public List<PlanListResponseDto> getRecruitmentPlans() {
        List<Plan> plans = planRepository.findAllByRecruitmentTrue();
        List<PlanListResponseDto> planList = new ArrayList<>();
        for (Plan plan : plans) {
            planList.add(PlanListResponseDto.builder()
                .planId(plan.getPlanId())
                .title(plan.getTitle())
                .location(plan.getLocation())
                .description(plan.getDescription())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .participantsCount(plan.getParticipantsCount())
                .planUserCount(plan.getPlanUsers().stream().filter(planUser -> planUser.getStatus() == PlanUser.StatusType.ACCEPTED).count())
                .planUserNickname(plan.getPlanUser().getNickname())
                .planThumbnailUrl(plan.getPlanThumbnailUrl())
                .bookmarkNumber(bookmarkService.countBookmark(plan))
                .likeNumber(likeService.countLike(plan))
                .build());
        }
        return planList;
    }
}
