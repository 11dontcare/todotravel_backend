package org.example.todotravel.domain.plan.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.chat.dto.request.ChatRoomCreateRequestDto;
import org.example.todotravel.domain.chat.service.impl.ChatRoomServiceImpl;
import org.example.todotravel.domain.plan.dto.request.PlanRequestDto;
import org.example.todotravel.domain.plan.dto.response.PlanListResponseDto;
import org.example.todotravel.domain.plan.dto.response.PlanResponseDto;
import org.example.todotravel.domain.plan.dto.response.PlanUserResponseDto;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.entity.PlanUser;
import org.example.todotravel.domain.plan.service.implement.PlanServiceImpl;
import org.example.todotravel.domain.plan.service.implement.PlanUserServiceImpl;
import org.example.todotravel.domain.user.dto.response.UserListResponseDto;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.impl.UserServiceImpl;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plan")
public class PlanController {
    private final PlanServiceImpl planService;
    private final UserServiceImpl userService;
    private final PlanUserServiceImpl planUserService;
    private final ChatRoomServiceImpl chatRoomService;

    //플랜 생성
    @PostMapping
    public ApiResponse<Long> createPlan(@Valid @RequestBody PlanRequestDto planRequestDto) {
        Plan plan = planService.createPlan(planRequestDto);

        // 채팅방 자동 생성
        chatRoomService.createChatRoom(plan);

        return new ApiResponse<>(true, "플랜 생성 성공", plan.getPlanId());
    }

    //플랜 수정 창
    @GetMapping("/{plan_id}")
    public ApiResponse<PlanResponseDto> updatePlan(@PathVariable("plan_id") Long planId) {
        PlanResponseDto plan = planService.getPlanForModify(planId);
        //수정 창에 수정하려는 plan 정보
        return new ApiResponse<>(true, "수정할 플랜 조회 성공", plan);
    }

    //플랜 수정
    @PutMapping("/{plan_id}")
    public ApiResponse<Long> updatePlan(@PathVariable("plan_id") Long planId, @Valid @RequestBody PlanRequestDto dto) {
        Plan plan = planService.updatePlan(planId, dto);
        return new ApiResponse<>(true, "플랜 수정 성공", planId);
    }

    //플랜 삭제
    @DeleteMapping("/{plan_id}")
    public ApiResponse<Plan> deletePlan(@PathVariable("plan_id") Long planId) {
        planService.deletePlan(planId);
        return new ApiResponse<>(true, "플랜 삭제 성공");
    }

    //플랜 사용자 초대(초대할 사용자 조회)
    @GetMapping("/{plan_id}/invite")
    public ApiResponse<List<UserListResponseDto>> invite(@PathVariable("plan_id") Long planId) {
        List<User> users = userService.getAllUsers();
        //해당 플랜에 참여하고 있지 않은 사용자만
        List<PlanUser> planUsers = planUserService.getAllPlanUser(planId);
        for (PlanUser planUser : planUsers) {
            if (users.contains(planUser.getUser()))
                users.remove(planUser.getUser());
        }
        List<UserListResponseDto> userList = new ArrayList<>();
        for (User user : users) {
            userList.add(UserListResponseDto.fromEntity(user));
        }
        return new ApiResponse<>(true, "사용자 목록 조회 성공", userList);
    }

    //플랜 사용자 초대
    @PostMapping("/{plan_id}/invite/{user_id}")
    public ApiResponse<PlanUserResponseDto> inviteUser(@PathVariable("plan_id") Long planId, @PathVariable("user_id") Long userId) {
        PlanUser planUser = planUserService.addPlanUser(planId, userId);
        PlanUserResponseDto planUserResponseDto = PlanUserResponseDto.fromEntity(planUser);
        return new ApiResponse<>(true, "사용자 초대 성공", planUserResponseDto);
    }

    //플랜 조회
    @GetMapping("/get")
    public ApiResponse<List<PlanListResponseDto>> viewPlanList() {
        List<PlanListResponseDto> planList = planService.getPublicPlans();
        return new ApiResponse<>(true, "플랜 목록 조회 성공", planList);
    }

    //플랜 상세 조회
    @GetMapping("/specific/{plan_id}")
    public ApiResponse<PlanResponseDto> viewPlan(@PathVariable("plan_id") Long planId) {
        PlanResponseDto planDetails = planService.getPlanDetails(planId);
        return new ApiResponse<>(true, "플랜 조회 성공", planDetails);
    }

    //플랜 불러오기
    @PostMapping("/{plan_id}/load")
    public ApiResponse<Long> loadPlan(@PathVariable("plan_id") Long planId) {
        Plan plan = planService.copyPlan(planId);
        return new ApiResponse<>(true, "플랜 불러오기 성공", plan.getPlanId());
    }

    //플랜 검색
    @GetMapping("/search/{keyword}")
    public ApiResponse<List<PlanListResponseDto>> searchPlans(@PathVariable("keyword") String keyword) {
        List<PlanListResponseDto> planList = planService.getSpecificPlans(keyword);
        return new ApiResponse<>(true, "플랜 검색 성공", planList);
    }
}
