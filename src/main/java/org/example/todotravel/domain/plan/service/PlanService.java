package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.dto.request.PlanRequestDto;
import org.example.todotravel.domain.plan.dto.response.PlanCountProjection;
import org.example.todotravel.domain.plan.dto.response.PlanListResponseDto;
import org.example.todotravel.domain.plan.dto.response.PlanResponseDto;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.example.todotravel.global.dto.PagedResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface PlanService {
    Plan createPlan(PlanRequestDto planRequestDto, MultipartFile file, User user);
    void deletePlan(Plan plan);
    void removeAllPlanByUser(User user);
    void removeJustPlan(Plan plan);
    Plan getPlan(Long planId);
    Plan updatePlan(Long planId, PlanRequestDto dto, MultipartFile planThumbnail);
    List<PlanListResponseDto> getPublicPlans();

    PlanResponseDto getPlanDetails(Long planId);
    Plan copyPlan(Long planId, User user);
    List<PlanListResponseDto> getSpecificPlans(String keyword);
    PlanResponseDto getPlanForModify(Long planId);
    void savePlan(Plan plan);

    List<PlanListResponseDto> getRecruitmentPlans();

    List<PlanListResponseDto> getRecentBookmarkedPlans(User user);
    List<PlanListResponseDto> getAllBookmarkedPlans(User user);
    List<PlanListResponseDto> getRecentLikedPlans(User user);
    List<PlanListResponseDto> getAllLikedPlans(User user);
    PagedResponseDto<PlanListResponseDto> getPopularPlansNotInRecruitment(int page, int size);
    PagedResponseDto<PlanListResponseDto> getPopularPlansWithFrontLocation(int page, int size, String frontLocation);
    PagedResponseDto<PlanListResponseDto> getPopularPlansWithAllLocation(int page, int size, String frontLocation, String location);

    PagedResponseDto<PlanListResponseDto> getRecentPlansByRecruitment(int page, int size, Boolean recruitment);
    PagedResponseDto<PlanListResponseDto> getRecentPlansWithFrontLocation(int page, int size, String frontLocation, Boolean recruitment);
    PagedResponseDto<PlanListResponseDto> getRecentPlansWithAllLocation(int page, int size, String frontLocation, String location, Boolean recruitment);

    PagedResponseDto<PlanListResponseDto> getRecentPlansRecruitmentByStartDate(int page, int size, Boolean recruitment, LocalDate startDate);
    PagedResponseDto<PlanListResponseDto> getRecentPlansWithFrontLocationAndStartDate(int page, int size, String frontLocation, Boolean recruitment, LocalDate startDate);
    PagedResponseDto<PlanListResponseDto> getRecentPlansWithAllLocationAndStartDate(int page, int size, String frontLocation, String location, Boolean recruitment, LocalDate startDate);

    Map<Long, PlanCountProjection> getBookmarkAndLikeCounts(List<Long> planIds);
    List<PlanListResponseDto> convertToPlanListResponseDto(List<Plan> plans);

    List<Plan> getAllPlanByPlanUser(User user);
  
    // 플랜 썸네일 이미지 등록
    void updateThumbnailImage(Long planId, MultipartFile file);
    Plan getThumbnailImageUrl(Long planId);
}
