package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.dto.request.PlanRequestDto;
import org.example.todotravel.domain.plan.dto.response.PlanListResponseDto;
import org.example.todotravel.domain.plan.dto.response.PlanResponseDto;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface PlanService {
    Plan createPlan(PlanRequestDto planRequestDto, User user);
    void deletePlan(Long planId);
    void removeAllPlanByUser(User user);
    Plan getPlan(Long planId);
    Plan updatePlan(Long planId, PlanRequestDto dto);
    List<PlanListResponseDto> getPublicPlans();

    PlanResponseDto getPlanDetails(Long planId);
    Plan copyPlan(Long planId, User user);
    List<PlanListResponseDto> getSpecificPlans(String keyword);
    PlanResponseDto getPlanForModify(Long planId);

    List<PlanListResponseDto> getRecentBookmarkedPlans(User user);
    List<PlanListResponseDto> getAllBookmarkedPlans(User user);
    List<PlanListResponseDto> getRecentLikedPlans(User user);
    List<PlanListResponseDto> getAllLikedPlans(User user);
    PlanListResponseDto convertToPlanListResponseDto(Plan plan);

    List<Plan> getAllPlanByPlanUser(User user);
  
    // 플랜 썸네일 이미지 등록
    void updateThumbnailImage(Long planId, MultipartFile file);
    Plan getThumbnailImageUrl(Long planId);
}
