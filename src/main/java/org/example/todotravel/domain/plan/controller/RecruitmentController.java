package org.example.todotravel.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.chat.dto.request.ChatMessageRequestDto;
import org.example.todotravel.domain.chat.dto.request.OneToOneChatRoomRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomResponseDto;
import org.example.todotravel.domain.chat.entity.ChatRoom;
import org.example.todotravel.domain.chat.service.ChatMessageService;
import org.example.todotravel.domain.chat.service.ChatRoomService;
import org.example.todotravel.domain.plan.dto.response.PlanListResponseDto;
import org.example.todotravel.domain.plan.dto.response.PlanUserResponseDto;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.entity.PlanUser;
import org.example.todotravel.domain.plan.service.PlanService;
import org.example.todotravel.domain.plan.service.PlanUserService;
import org.example.todotravel.global.controller.ApiResponse;
import org.example.todotravel.global.dto.PagedResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RecruitmentController {
    private final PlanService planService;
    private final PlanUserService planUserService;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    //모집중으로 플랜 변경(플랜 모집하기)
    @PutMapping("/recruitment/{plan_id}")
    public ApiResponse<Long> recruitmentPlan(@PathVariable("plan_id") Long planId, @RequestBody Integer participantsCount){
        Plan plan = planService.getPlan(planId);
        for (PlanUser planUser : plan.getPlanUsers()){
            if (planUser.getStatus() != PlanUser.StatusType.ACCEPTED) {
                planUserService.removePlanUserFromPlan(plan, planUser.getUser());
            }
        }
        plan = plan.toBuilder()
                .recruitment(true)
                .isPublic(true)
                .participantsCount(participantsCount)
                .build();
        planService.savePlan(plan);
        return new ApiResponse<>(true, "플랜 모집 성공", planId);
    }

    //플랜 모집 취소
    @PutMapping("/recruitment/cancel/{plan_id}")
    public ApiResponse<Long> cancelRecruitmentPlan(@PathVariable("plan_id") Long planId){
        Plan plan = planService.getPlan(planId);
        for (PlanUser planUser : plan.getPlanUsers()){
            if (planUser.getStatus() != PlanUser.StatusType.ACCEPTED) {
                planUserService.removePlanUserFromPlan(plan, planUser.getUser());
            }
        }
        plan = plan.toBuilder()
                .recruitment(false)
                .participantsCount(null)
                .build();
        planService.savePlan(plan);
        return new ApiResponse<>(true, "플랜 모집 취소 성공", planId);
    }

    //플랜 모집글 리스트 조회
    @GetMapping("/plan/recruitment")
    public ApiResponse<List<PlanListResponseDto>> viewRecruitmentPlans(){
        List<PlanListResponseDto> recruitmentList = planService.getRecruitmentPlans();
        return new ApiResponse<>(true, "플랜 모집글 리스트 조회 성공", recruitmentList);
    }

    //모집중인 플랜에 참가 요청
    @PostMapping("/recruitment/{plan_id}/request/{user_id}")
    public ApiResponse<PlanUserResponseDto> requestRecruitment(@PathVariable("plan_id") Long planId, @PathVariable("user_id") Long userId) {
        PlanUser planUser = planUserService.addPlanUser(planId, userId);

        //일대일 채팅방 생성
        OneToOneChatRoomRequestDto chatRoomDto = OneToOneChatRoomRequestDto.builder()
                .senderId(userId)
                .receiverId(planUser.getPlan().getPlanUser().getUserId())
                .build();
        ChatRoomResponseDto chatRoom = chatRoomService.createOneToOneChatRoom(chatRoomDto);

        //자동으로 요청 메시지 생성
        ChatMessageRequestDto messageDto = ChatMessageRequestDto.builder()
                .userId(userId)
                .roomId(chatRoom.getRoomId())
                .nickname(planUser.getUser().getNickname())
                .content("[" + planUser.getPlan().getTitle() + "] 플랜에 " + planUser.getUser().getNickname() + "님이 참가하기를 요청했습니다.")
                .build();
        chatMessageService.saveChatMessage(messageDto);

        PlanUserResponseDto planUserResponseDto = PlanUserResponseDto.fromEntity(planUser);
        return new ApiResponse<>(true, "플랜 참가 요청 성공", planUserResponseDto);
    }

    //참가 요청 승인 (모집중인 플랜의 생성자가 승인)
    @PutMapping("/recruitment/{plan_participant_id}/accept")
    public ApiResponse<Long> acceptInvite(@PathVariable("plan_participant_id") Long planParticipantId) {
        // 플랜 모집 참가 수락 처리
        PlanUser planUser = planUserService.accepted(planParticipantId);

        // 채팅방에 자동 참여
        ChatRoom chatRoom = chatRoomService.getChatRoomByPlanId(planUser.getPlan().getPlanId());
        chatRoomService.addUserToChatRoom(chatRoom.getRoomId(), planUser.getUser().getUserId());

        return new ApiResponse<>(true, "플랜 모집 참가 승인 성공", planUser.getPlanParticipantId());
    }

    //참가 요청 거절 (모집중인 플랜의 생성자가 거절)
    @PutMapping("/recruitment/{plan_participant_id}/reject")
    public ApiResponse<Long> rejectRecruitment(@PathVariable("plan_participant_id") Long planParticipantId) {
        PlanUser planUser = planUserService.rejected(planParticipantId);
        return new ApiResponse<>(true, "플랜 모집 참가 거절 성공", planUser.getPlanParticipantId());
    }

    // 모집 플랜 최신순으로 가져오기 (Public, Recruitment)
    @GetMapping("/recruitment/recent")
    public ApiResponse<?> getRecentPlans(@RequestParam(name = "page", defaultValue = "0") int page,
                                         @RequestParam(name = "size", defaultValue = "12") int size) {
        PagedResponseDto<PlanListResponseDto> planList = planService.getRecentPlansByRecruitment(page, size, true);
        return new ApiResponse<>(true, "최신순 플랜 조회에 성공했습니다.", planList);
    }

    // 행정구역별 모집 플랜 최신순 가져오기
    @GetMapping("/recruitment/recent/frontLocation")
    public ApiResponse<?> getRecentPlansByFrontLocation(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "12") int size,
                                                        @RequestParam String frontLocation) {
        PagedResponseDto<PlanListResponseDto> planList = planService.getRecentPlansWithFrontLocation(page, size, frontLocation, true);
        return new ApiResponse<>(true, "행정구역별 최신순 플랜 조회에 성공했습니다.", planList);
    }

    // 행정구역+도시별 모집 플랜 최신순 가져오기
    @GetMapping("/recruitment/recent/location")
    public ApiResponse<?> getRecentPlansByLocation(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "12") int size,
                                                   @RequestParam String frontLocation,
                                                   @RequestParam String location) {
        PagedResponseDto<PlanListResponseDto> planList = planService.getRecentPlansWithAllLocation(page, size, frontLocation, location, true);
        return new ApiResponse<>(true, "행정구역+도시별 최신순 플랜 조회에 성공했습니다.", planList);
    }

    // 모집 플랜 날짜, 최신순으로 가져오기 (Public, Recruitment)
    @GetMapping("/recruitment/recent/startDate")
    public ApiResponse<?> getRecentPlansByStartDate(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "12") int size,
                                         @RequestParam LocalDate startDate) {
        PagedResponseDto<PlanListResponseDto> planList = planService.getRecentPlansRecruitmentByStartDate(page, size, true, startDate);

        return new ApiResponse<>(true, "최신순 플랜 조회에 성공했습니다.", planList);
    }

    // 날짜, 행정구역별 모집 플랜 최신순 가져오기
    @GetMapping("/recruitment/recent/frontLocation/startDate")
    public ApiResponse<?> getRecentPlansByFrontLocationAndStartDate(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "12") int size,
                                                        @RequestParam String frontLocation,
                                                        @RequestParam LocalDate startDate) {
        PagedResponseDto<PlanListResponseDto> planList = planService.getRecentPlansWithFrontLocationAndStartDate(page, size, frontLocation, true, startDate);
        return new ApiResponse<>(true, "행정구역별 최신순 플랜 조회에 성공했습니다.", planList);
    }

    // 날짜, 행정구역+도시별 모집 플랜 최신순 가져오기
    @GetMapping("/recruitment/recent/location/startDate")
    public ApiResponse<?> getRecentPlansByLocationAndStartDate(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "12") int size,
                                                   @RequestParam String frontLocation,
                                                   @RequestParam String location,
                                                   @RequestParam LocalDate startDate) {
        PagedResponseDto<PlanListResponseDto> planList = planService.getRecentPlansWithAllLocationAndStartDate(page, size, frontLocation, location, true, startDate);
        return new ApiResponse<>(true, "행정구역+도시별 최신순 플랜 조회에 성공했습니다.", planList);
    }
}
