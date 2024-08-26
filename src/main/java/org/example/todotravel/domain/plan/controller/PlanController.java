package org.example.todotravel.domain.plan.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.chat.entity.ChatRoom;
import org.example.todotravel.domain.chat.service.ChatRoomService;
import org.example.todotravel.domain.plan.dto.request.PlanRequestDto;
import org.example.todotravel.domain.plan.dto.request.PlanThumbnailRequestDto;
import org.example.todotravel.domain.plan.dto.response.PlanListResponseDto;
import org.example.todotravel.domain.plan.dto.response.PlanResponseDto;
import org.example.todotravel.domain.plan.dto.response.PlanUserResponseDto;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.entity.PlanUser;
import org.example.todotravel.domain.plan.service.PlanService;
import org.example.todotravel.domain.plan.service.PlanUserService;
import org.example.todotravel.domain.plan.service.ScheduleService;
import org.example.todotravel.domain.user.dto.request.UserProfileImageRequestDTO;
import org.example.todotravel.domain.user.dto.response.UserListResponseDto;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.UserService;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plan")
public class PlanController {
    private final PlanService planService;
    private final UserService userService;
    private final PlanUserService planUserService;
    private final ChatRoomService chatRoomService;
    private final ScheduleService scheduleService;

    //플랜 생성
    @PostMapping
    public ApiResponse<Long> createPlan(@Valid @RequestBody PlanRequestDto planRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        Plan plan = planService.createPlan(planRequestDto, user);

        // 채팅방 자동 생성
        chatRoomService.createChatRoom(plan);

        return new ApiResponse<>(true, "플랜 생성 성공", plan.getPlanId());
    }

    //플랜 가져오기(모든 플랜 관련 정보(플랜 정보, 북마크, 좋아요, 댓글, 일정))
    @GetMapping("/{plan_id}")
    public ApiResponse<PlanResponseDto> getPlan(@PathVariable("plan_id") Long planId) {
        PlanResponseDto planDetails = planService.getPlanDetails(planId);
        planDetails = planDetails.toBuilder()
                .scheduleList(scheduleService.getSchedulesByPlan(planId))
                .build();
        return new ApiResponse<>(true, "플랜 조회 성공", planDetails);
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
        Plan plan = planService.getPlan(planId);

        // 플랜 삭제 시 채팅방도 삭제
        ChatRoom chatRoom = chatRoomService.getChatRoomByPlanId(planId);
        chatRoomService.deleteChatRoomAndMessage(chatRoom.getRoomId());
        planUserService.removePlanUserFromOwnPlan(plan);
        planService.deletePlan(plan);

        return new ApiResponse<>(true, "플랜 삭제 성공");
    }

    //플랜에 사용자 초대하기 위해서 사용자 리스트 조회(현재 전체 사용자 조회, 추후 팔로잉 조회로 변경 예정)
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

    //플랜에 사용자 초대
    @PostMapping("/{plan_id}/invite/{user_id}")
    public ApiResponse<PlanUserResponseDto> inviteUser(@PathVariable("plan_id") Long planId, @PathVariable("user_id") Long userId) {
        PlanUser planUser = planUserService.addPlanUser(planId, userId);
        PlanUserResponseDto planUserResponseDto = PlanUserResponseDto.fromEntity(planUser);
        return new ApiResponse<>(true, "사용자 초대 성공", planUserResponseDto);
    }

    //전체 플랜 조회 - 공개 상태인 플랜만
    @GetMapping("/public")
    public ApiResponse<List<PlanListResponseDto>> viewPublicPlans() {
        List<PlanListResponseDto> planList = planService.getPublicPlans();
        return new ApiResponse<>(true, "플랜 목록 조회 성공", planList);
    }

    //플랜 가져오기(댓글x, 일정x)   (플랜 정보, 북마크, 좋아요)
    //상단의 getPlan이 플랜의 모든 관련 정보들을 return
    @GetMapping("/public/{plan_id}")
    public ApiResponse<PlanResponseDto> getPublicPlan(@PathVariable("plan_id") Long planId){
        PlanResponseDto plan = planService.getPlanForModify(planId);
        return new ApiResponse<>(true, "수정할 플랜 조회 성공", plan);
    }

    //플랜 copy해서 만들기(불러오기 기능)
    @PostMapping("/{plan_id}/load")
    public ApiResponse<Long> getLoadPlan(@PathVariable("plan_id") Long planId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        Plan plan = planService.copyPlan(planId, user);
        return new ApiResponse<>(true, "플랜 불러오기 성공", plan.getPlanId());
    }

    //플랜 검색
    @GetMapping("/search/{keyword}")
    public ApiResponse<List<PlanListResponseDto>> searchPlans(@PathVariable("keyword") String keyword) {
        List<PlanListResponseDto> planList = planService.getSpecificPlans(keyword);
        return new ApiResponse<>(true, "플랜 검색 성공", planList);
    }

    // 플랜 썸네일 이미지 등록
    @PostMapping("/thumbnail/{plan_id}")
    public ApiResponse<PlanThumbnailRequestDto> uploadThumbnailImage(@PathVariable("plan_id") Long planId, @RequestParam("file")
                                                                     MultipartFile file) {
        try {
            planService.updateThumbnailImage(planId, file);

            Plan plan = planService.getThumbnailImageUrl(planId);
            String thumnailImageUrl = plan.getPlanThumbnailUrl();

            PlanThumbnailRequestDto response = new PlanThumbnailRequestDto(planId, thumnailImageUrl);
            return new ApiResponse<>(true, "썸네일 이미지가 성공적으로 업로드 되었습니다.", response);
        } catch (Exception e) {
            return new ApiResponse<>(false, "썸네일 이미지 등록에 실패했습니다.");
        }
    }
}
