package org.example.todotravel.domain.plan.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.notification.dto.request.AlarmRequestDto;
import org.example.todotravel.domain.notification.service.AlarmService;
import org.example.todotravel.domain.plan.dto.response.PlanListResponseDto;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.entity.PlanUser;
import org.example.todotravel.domain.plan.repository.PlanUserRepository;
import org.example.todotravel.domain.plan.service.PlanService;
import org.example.todotravel.domain.plan.service.PlanUserService;
import org.example.todotravel.domain.user.dto.response.UserProfileResponseDto;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.UserService;
import org.example.todotravel.global.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanUserServiceImpl implements PlanUserService {
    private final PlanUserRepository planUserRepository;
    private final PlanService planService;
    private final UserService userService;
    private final AlarmService alarmService; //알림 자동 생성

    //플랜 초대
    @Override
    @Transactional
    public PlanUser addPlanUser(Long planId, Long userId) {
        Plan plan = planService.getPlan(planId);
        User user = userService.getUserByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        PlanUser planUser = PlanUser.builder()
            .status(PlanUser.StatusType.PENDING)
            .user(user)
            .plan(plan)
            .build();

        PlanUser newPlanUser = planUserRepository.save(planUser);


        AlarmRequestDto requestDto = new AlarmRequestDto(plan.getPlanUser().getUserId(),
            "[" + plan.getTitle() + "] 플랜에 " + user.getNickname() + "님이 초대 되었습니다.");
        alarmService.createAlarm(requestDto);

        return newPlanUser;
    }

    //플랜 초대 거절
    @Override
    @Transactional
    public PlanUser rejected(Long planParticipantId) {
        PlanUser planUser = planUserRepository.findById(planParticipantId).orElseThrow(() -> new RuntimeException("플랜 참여 상태를 찾을 수 없습니다."));
        planUser.setStatus(PlanUser.StatusType.REJECTED);
        return planUserRepository.save(planUser);
    }

    //플랜 초대 수락
    @Override
    @Transactional
    public PlanUser accepted(Long planParticipantId) {
        PlanUser planUser = planUserRepository.findById(planParticipantId).orElseThrow(() -> new RuntimeException("플랜 참여 상태를 찾을 수 없습니다."));
        planUser.setStatus(PlanUser.StatusType.ACCEPTED);
        return planUserRepository.save(planUser);
    }

    //플랜 참여자 조회
    @Override
    @Transactional(readOnly = true)
    public List<PlanUser> getAllPlanUser(Long planId) {
        Plan plan = planService.getPlan(planId);
        return planUserRepository.findAllByPlan(plan);
    }

    @Override
    @Transactional
    public void removePlanUser(Long planId, Long userId) {
        Plan plan = planService.getPlan(planId);
        User user = userService.getUserByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        planUserRepository.deletePlanUserByPlanAndUser(plan, user);
    }

    // 회원 탈퇴 시 사용자가 생성한 플랜에서 모든 사용자 제거
    @Override
    @Transactional
    public void removePlanUserFromOwnPlan(Plan plan) {
        planUserRepository.deleteAllByPlan(plan);
    }

    // 회원 탈퇴 시 참여하고 있던 모든 플랜에서 사용자 제거
    @Override
    @Transactional
    public void removePlanUserFromPlan(Plan plan, User user) {
        planUserRepository.deleteByPlanIdAndUserId(plan.getPlanId(), user.getUserId());
    }

    // 사용자 프로필 조회
    @Override
    @Transactional(readOnly = true)
    public UserProfileResponseDto getUserProfile(String subject, User user, boolean isFollowing) {
        Long userId = user.getUserId();
        List<PlanListResponseDto> planList = getAllPlansByUserAndStatus(userId);
        int planCount = planList.size();

        if (subject.equals("my")) {
            planList = planCount > 3 ? planList.subList(0, 3) : planList;
        }

        return UserProfileResponseDto.builder()
            .userId(userId)
            .nickname(user.getNickname())
            .gender(user.getGender())
            .age(Period.between(user.getBirthDate(), LocalDate.now()).getYears())
            .info(user.getInfo())
            .isFollowing(isFollowing)
            .followerCount(user.getFollowings().size()) // 팔로워 수 : 나를 팔로잉 하는 수
            .followingCount(user.getFollowers().size()) // 팔로잉 수 : 내가 팔로잉 하는 수
            .planCount(planCount)
            .planList(planList)
            .build();
    }

    // 특정 사용자가 관여한 모든 플랜 조회
    @Override
    @Transactional(readOnly = true)
    public List<Plan> getAllPlansByUser(User user) {
        return planUserRepository.findAllPlansByUserId(user.getUserId(), PlanUser.StatusType.ACCEPTED);
    }

    // 특정 사용자가 참여한 모든 플랜 DTO로 조회
    @Override
    @Transactional(readOnly = true)
    public List<PlanListResponseDto> getAllPlansByUserAndStatus(Long userId) {
        List<Plan> plans = planUserRepository.findAllPlansByUserId(userId, PlanUser.StatusType.ACCEPTED);
        return plans.stream()
            .map(planService::convertToPlanListResponseDto)
            .collect(Collectors.toList());
    }

    // 특정 사용자가 참여한 최근 플랜 3개 조회
    @Override
    @Transactional(readOnly = true)
    public List<PlanListResponseDto> getRecentPlansByUser(Long userId) {
        List<Plan> plans = planUserRepository.findAllPlansByUserId(userId, PlanUser.StatusType.ACCEPTED);
        plans = plans.size() > 3 ? plans.subList(0, 3) : plans;

        return plans.stream()
            .map(planService::convertToPlanListResponseDto)
            .collect(Collectors.toList());
    }
}
