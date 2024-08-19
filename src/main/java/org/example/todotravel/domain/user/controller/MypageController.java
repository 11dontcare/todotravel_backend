package org.example.todotravel.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.dto.response.CommentSummaryResponseDto;
import org.example.todotravel.domain.plan.dto.response.PlanListResponseDto;
import org.example.todotravel.domain.plan.service.CommentService;
import org.example.todotravel.domain.plan.service.PlanService;
import org.example.todotravel.domain.plan.service.PlanUserService;
import org.example.todotravel.domain.user.dto.request.FollowRequestDto;
import org.example.todotravel.domain.user.dto.request.NicknameRequestDto;
import org.example.todotravel.domain.user.dto.request.PasswordUpdateRequestDto;
import org.example.todotravel.domain.user.dto.response.FollowResponseDto;
import org.example.todotravel.domain.user.dto.response.MyProfileResponseDto;
import org.example.todotravel.domain.user.dto.response.UserDetailResponseDto;
import org.example.todotravel.domain.user.dto.response.UserProfileResponseDto;
import org.example.todotravel.domain.user.service.FollowService;
import org.example.todotravel.domain.user.service.UserService;
import org.example.todotravel.global.controller.ApiResponse;
import org.example.todotravel.global.jwt.util.AuthenticationUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageController {
    private final AuthenticationUtil authenticationUtil;
    private final PasswordEncoder passwordEncoder;
    private final PlanUserService planUserService;
    private final CommentService commentService;
    private final FollowService followService;
    private final PlanService planService;
    private final UserService userService;

    // 특정 사용자 프로필 보기 (본인, 타인에 대한 처리)
    @GetMapping("/profile/{user_id}")
    public ApiResponse<?> showProfile(@PathVariable("user_id") Long userId, Authentication authentication) {
        // 본인인 경우
        if (authenticationUtil.isAuthenticatedUser(authentication, userId)) {
            UserProfileResponseDto baseProfile = planUserService.getUserProfile("my", userId);
            List<PlanListResponseDto> recentBookmarks = planService.getRecentBookmarkedPlans(userId);
            List<PlanListResponseDto> recentLikes = planService.getRecentLikedPlans(userId);
            List<CommentSummaryResponseDto> recentComments = commentService.getRecentCommentedPlansByUser(userId);
            MyProfileResponseDto myProfileResponseDto = MyProfileResponseDto.from(baseProfile, recentBookmarks, recentLikes, recentComments);
            return new ApiResponse<>(true, "본인 마이페이지 조회에 성공했습니다.", myProfileResponseDto);
        } else { // 타인인 경우
            UserProfileResponseDto userProfileResponseDto = planUserService.getUserProfile("other", userId);
            return new ApiResponse<>(true, "타인 마이페이지 조회에 성공했습니다.", userProfileResponseDto);
        }
    }

    // 내 개인정보 조회
    @GetMapping("/personal-profile/{user_id}")
    public ApiResponse<?> getPersonalProfile(@PathVariable("user_id") Long userId, Authentication authentication) {
        // 본인 확인
        if (!authenticationUtil.isAuthenticatedUser(authentication, userId)) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        UserDetailResponseDto userDetailResponseDto = UserDetailResponseDto.from(userService.getUserById(userId));
        return new ApiResponse<>(true, "사용자 개인 정보 조회에 성공했습니다.", userDetailResponseDto);
    }

    // 닉네임 변경
    @PutMapping("/nickname")
    public ApiResponse<?> changeNickname(@RequestBody NicknameRequestDto dto, Authentication authentication) {
        // 본인 확인
        if (!authenticationUtil.isAuthenticatedUser(authentication, dto.getUserId())) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        userService.updateNickname(dto);
        return new ApiResponse<>(true, "닉네임 변경을 완료했습니다.");
    }

    // 비밀번호 변경 - UserController 에도 있지만, 마이페이지에서 기존 비밀번호를 알아야 가능한 변경
    @PutMapping("/password")
    public ApiResponse<?> changePassword(@RequestBody PasswordUpdateRequestDto dto, Authentication authentication) {
        // 본인 확인
        if (!authenticationUtil.isAuthenticatedUser(authentication, dto.getUserId())) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
        userService.updatePassword(dto, passwordEncoder);
        return new ApiResponse<>(true, "비밀번호 변경을 완료했습니다.");
    }

    // 회원 탈퇴 - 모든 정보를 삭제?
//    @DeleteMapping("/{user_id}/withdraw")
//    public ApiResponse<?> deleteAllUserInformation(@PathVariable("user_id") Long userId) {
//
//    }

    // 사용자 팔로우
    @PostMapping("/follow")
    public ApiResponse<?> doFollowing(@RequestBody FollowRequestDto dto) {
        followService.startFollowing(dto);
        return new ApiResponse<>(true, "팔로우에 성공했습니다.");
    }

    // 팔로우 취소
    @DeleteMapping("/cancel-follow")
    public ApiResponse<?> cancelFollowing(@RequestBody FollowRequestDto dto) {
        followService.stopFollowing(dto);
        return new ApiResponse<>(true, "팔로우 취소에 성공했습니다.");
    }

    // 팔로잉 조회
    @GetMapping("/{user_id}/following")
    public ApiResponse<?> getFollowing(@PathVariable("user_id") Long userId) {
        List<FollowResponseDto> followingList = followService.getFollowing(userId);
        return new ApiResponse<>(true, "팔로잉 사용자 조회에 성공했습니다.", followingList);
    }

    // 팔로워 조회
    @GetMapping("/{user_id}/follower")
    public ApiResponse<?> getFollower(@PathVariable("user_id") Long userId) {
        List<FollowResponseDto> followerList = followService.getFollower(userId);
        return new ApiResponse<>(true, "팔로워 사용자 조회에 성공했습니다.", followerList);
    }

    // 내 여행 전체 조회
    @GetMapping("/{user_id}/my-trip")
    public ApiResponse<?> getAllPlans(@PathVariable("user_id") Long userId) {
        List<PlanListResponseDto> planList = planUserService.getAllPlansByUser(userId);
        return new ApiResponse<>(true, "전체 여행 조회에 성공했습니다.", planList);
    }

    // 북마크 플랜 전체 조회
    @GetMapping("/{user_id}/my-bookmark")
    public ApiResponse<?> getAllBookmarkedPlans(@PathVariable("user_id") Long userId, Authentication authentication) {
        // 본인 확인
        if (!authenticationUtil.isAuthenticatedUser(authentication, userId)) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        List<PlanListResponseDto> planList = planService.getAllBookmarkedPlans(userId);
        return new ApiResponse<>(true, "북마크한 여행 조회에 성공했습니다.", planList);
    }

    // 좋아요한 플랜 전체 조회
    @GetMapping("/{user_id}/my-like")
    public ApiResponse<?> getAllLikedPlans(@PathVariable("user_id") Long userId, Authentication authentication) {
        // 본인 확인
        if (!authenticationUtil.isAuthenticatedUser(authentication, userId)) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        List<PlanListResponseDto> planList = planService.getAllLikedPlans(userId);
        return new ApiResponse<>(true, "좋아요한 여행 조회에 성공했습니다.", planList);
    }

    // 댓글 단 플랜 전체 조회
    @GetMapping("/{user_id}/my-comment")
    public ApiResponse<?> getAllCommentedPlans(@PathVariable("user_id") Long userId, Authentication authentication) {
        // 본인 확인
        if (!authenticationUtil.isAuthenticatedUser(authentication, userId)) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        List<CommentSummaryResponseDto> planList = commentService.getAllCommentedPlansByUser(userId);
        return new ApiResponse<>(true, "댓글을 단 여행 조회에 성공했습니다.", planList);
    }
}
