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
import org.example.todotravel.domain.user.dto.response.UserListResponseDto;
import org.example.todotravel.domain.user.entity.Follow;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.UserService;
import org.example.todotravel.global.controller.ApiResponse;
import org.example.todotravel.global.dto.PagedResponseDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

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
    public ApiResponse<Long> createPlan(@Valid @RequestPart("planRequestDto") PlanRequestDto planRequestDto,
                                        @RequestParam(value = "planThumbnail", required = false) MultipartFile planThumbnail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        Plan plan = planService.createPlan(planRequestDto, planThumbnail, user);

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

    // 플랜 수정
    @PutMapping("/{plan_id}")
    public ApiResponse<Long> updatePlan(@PathVariable("plan_id") Long planId,
                                        @RequestPart("planRequestDto") PlanRequestDto dto,
                                        @RequestPart(value = "planThumbnail", required = false) MultipartFile planThumbnail) {
        // @RequestPart - Multipart/form-data가 포함되어 있는 경우에 사용, 없다면 @RequestBody와 동일
        Plan plan = planService.updatePlan(planId, dto, planThumbnail);
        return new ApiResponse<>(true, "플랜 수정 성공", planId);
    }

    //플랜 삭제
    @DeleteMapping("/{plan_id}")
    public ApiResponse<Plan> removePlan(@PathVariable("plan_id") Long planId) {
        Plan plan = planService.getPlan(planId);

        // 플랜 삭제 시 채팅방도 삭제
        ChatRoom chatRoom = chatRoomService.getChatRoomByPlanId(planId);
        chatRoomService.removeChatRoomAndMessage(chatRoom.getRoomId());
        planUserService.removePlanUserFromOwnPlan(plan);
        planService.removePlan(plan);

        return new ApiResponse<>(true, "플랜 삭제 성공");
    }

    //플랜에 사용자 초대하기 위해서 사용자 리스트 조회(팔로워 조회로 변경)
    @GetMapping("/{plan_id}/invite")
    public ApiResponse<List<UserListResponseDto>> invite(@PathVariable("plan_id") Long planId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());

        Set<Follow> followers = user.getFollowings();
        List<User> users = new ArrayList<>();
        for (Follow follow : followers) {
            users.add(follow.getFollowerUser());
        }
        //해당 플랜에 참여하고 있지 않은 사용자만
        List<PlanUser> planUsers = planUserService.getAllPlanUser(planId);
        for (PlanUser planUser : planUsers) {
            if (users.contains(planUser.getUser()))
                users.remove(planUser.getUser());
        }
        List<UserListResponseDto> userList = new ArrayList<>();
        for (User resultUser : users) {
            userList.add(UserListResponseDto.fromEntity(resultUser));
        }
        return new ApiResponse<>(true, "팔로워 목록 조회 성공", userList);
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
    public ApiResponse<List<PlanListResponseDto>> getPublicPlans() {
        List<PlanListResponseDto> planList = planService.getPublicPlans();
        return new ApiResponse<>(true, "플랜 목록 조회 성공", planList);
    }

    // 플랜 기본 인기순으로 가져오기 (Public, No Recruitment)
    @GetMapping("/popular")
    public ApiResponse<?> getPopularPlans(@RequestParam(name = "page", defaultValue = "0") int page,
                                          @RequestParam(name = "size", defaultValue = "12") int size) {
        PagedResponseDto<PlanListResponseDto> planList = planService.getPopularPlansNotInRecruitment(page, size);
        return new ApiResponse<>(true, "다음 인기순 플랜 조회에 성공했습니다.", planList);
    }

    // 행정구역별 인기순 플랜 가져오기
    @GetMapping("/popular/frontLocation")
    public ApiResponse<?> getPopularPlansByFrontLocation(@RequestParam(name = "page", defaultValue = "0") int page,
                                                         @RequestParam(name = "size", defaultValue = "12") int size,
                                                         @RequestParam(name = "frontLocation") String frontLocation) {
        PagedResponseDto<PlanListResponseDto> planList = planService.getPopularPlansWithFrontLocation(page, size, frontLocation);
        return new ApiResponse<>(true, "행정구역별 인기순 플랜 조회에 성공했습니다.", planList);
    }

    // 행정구역+도시별 인기순 플랜 가져오기
    @GetMapping("/popular/location")
    public ApiResponse<?> getPopularPlansByLocation(@RequestParam(name = "page", defaultValue = "0") int page,
                                                    @RequestParam(name = "size", defaultValue = "12") int size,
                                                    @RequestParam(name = "frontLocation") String frontLocation,
                                                    @RequestParam(name = "location") String location) {
        PagedResponseDto<PlanListResponseDto> planList = planService.getPopularPlansWithAllLocation(page, size, frontLocation, location);
        return new ApiResponse<>(true, "행정구역+도시별 인기순 플랜 조회에 성공했습니다.", planList);
    }

    // 플랜 기본 최신순으로 가져오기 (Public, No Recruitment)
    @GetMapping("/recent")
    public ApiResponse<?> getRecentPlans(@RequestParam(name = "page", defaultValue = "0") int page,
                                         @RequestParam(name = "size", defaultValue = "12") int size) {
        PagedResponseDto<PlanListResponseDto> planList = planService.getRecentPlansByRecruitment(page, size, false);
        return new ApiResponse<>(true, "최신순 플랜 조회에 성공했습니다.", planList);
    }

    // 행정구역별 최신순 플랜 가져오기
    @GetMapping("/recent/frontLocation")
    public ApiResponse<?> getRecentPlansByFrontLocation(@RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "12") int size,
                                                        @RequestParam(name = "frontLocation") String frontLocation) {
        PagedResponseDto<PlanListResponseDto> planList = planService.getRecentPlansWithFrontLocation(page, size, frontLocation, false);
        return new ApiResponse<>(true, "행정구역별 최신순 플랜 조회에 성공했습니다.", planList);
    }

    // 행정구역+도시별 최신순 플랜 가져오기
    @GetMapping("/recent/location")
    public ApiResponse<?> getRecentPlansByLocation(@RequestParam(name = "page", defaultValue = "0") int page,
                                                   @RequestParam(name = "size", defaultValue = "12") int size,
                                                   @RequestParam(name = "frontLocation") String frontLocation,
                                                   @RequestParam(name = "location") String location) {
        PagedResponseDto<PlanListResponseDto> planList = planService.getRecentPlansWithAllLocation(page, size, frontLocation, location, false);
        return new ApiResponse<>(true, "행정구역+도시별 최신순 플랜 조회에 성공했습니다.", planList);
    }

    //플랜 가져오기(댓글x, 일정x)   (플랜 정보, 북마크, 좋아요)
    //상단의 getPlan이 플랜의 모든 관련 정보들을 return
    @GetMapping("/public/{plan_id}")
    public ApiResponse<PlanResponseDto> getPublicPlan(@PathVariable("plan_id") Long planId) {
        PlanResponseDto plan = planService.getPlanForModify(planId);
        return new ApiResponse<>(true, "수정할 플랜 조회 성공", plan);
    }

    //플랜 copy해서 만들기(불러오기 기능)
    @PostMapping("/{plan_id}/load")
    public ApiResponse<Long> getLoadPlan(@PathVariable("plan_id") Long planId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        Plan plan = planService.copyPlan(planId, user);

        // 채팅방 자동 생성
        chatRoomService.createChatRoom(plan);

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
