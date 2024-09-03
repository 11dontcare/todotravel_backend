package org.example.todotravel.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todotravel.domain.plan.dto.response.CommentSummaryResponseDto;
import org.example.todotravel.domain.plan.dto.response.PlanListResponseDto;
import org.example.todotravel.domain.plan.service.*;
import org.example.todotravel.domain.user.dto.request.*;
import org.example.todotravel.domain.user.dto.response.*;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.FollowService;
import org.example.todotravel.domain.user.service.UserService;
import org.example.todotravel.domain.user.service.UserWithdrawalService;
import org.example.todotravel.global.controller.ApiResponse;
import org.example.todotravel.global.dto.PagedResponseDto;
import org.example.todotravel.global.jwt.util.AuthenticationUtil;
import org.example.todotravel.global.jwt.util.JwtTokenizer;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageController {
    private final AuthenticationUtil authenticationUtil;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;

    private final UserWithdrawalService userWithdrawalService;
    private final PlanUserService planUserService;
    private final CommentService commentService;
    private final FollowService followService;
    private final PlanService planService;
    private final UserService userService;

    // 프론트할 때 컴포넌트를 나누지 못해서 한 번에 처리된다,,
    // 특정 사용자 프로필 보기 (본인, 타인에 대한 처리)
    @GetMapping("/profile/{nickname}")
    public ApiResponse<?> showProfile(@PathVariable("nickname") String nickname, Authentication authentication) {
        // 해당 사용자 찾기
        User user = userService.getUserIdByNickname(nickname);

        // 본인인 경우
        if (authenticationUtil.isAuthenticatedUser(authentication, user)) {
            UserProfileResponseDto baseProfile = planUserService.getUserProfile("my", user, false);   // 프로필 및 본인 여행 조회
            List<PlanListResponseDto> recruitingPlans = planUserService.getOwnRecruitmentPlansLimit4(user);             // 모집 중인 플랜 조회
            List<PlanListResponseDto> recentBookmarks = planService.getRecentBookmarkedPlans(user);                     // 북마크한 플랜 조회
            List<PlanListResponseDto> recentLikes = planService.getRecentLikedPlans(user);                              // 좋아요한 플랜 조회
            List<CommentSummaryResponseDto> recentComments = commentService.getRecentCommentedPlansByUser(user);        // 댓글 단 플랜 조회

            // 한 번에 반환
            MyProfileResponseDto myProfileResponseDto = MyProfileResponseDto.from(baseProfile, recruitingPlans, recentBookmarks, recentLikes, recentComments);
            return new ApiResponse<>(true, "본인 마이페이지 조회에 성공했습니다.", myProfileResponseDto);
        } else { // 타인인 경우
            boolean isFollowing = followService.checkFollowing(authentication, user); // 팔로우 중인지 확인
            UserProfileResponseDto userProfileResponseDto = planUserService.getUserProfile("other", user, isFollowing);
            return new ApiResponse<>(true, "타인 마이페이지 조회에 성공했습니다.", userProfileResponseDto);
        }
    }

    // 소개글 업데이트
    @PutMapping("/update-info")
    public ApiResponse<?> updateInfo(@Valid @RequestBody UserInfoRequestDto dto, Authentication authentication) {
        // 해당 사용자 찾은 후 본인 확인 수행
        User user = userService.getUserById(dto.getUserId());
        verifyAuthenticatedUser(authentication, user);

        userService.updateUserInfo(user, dto);
        return new ApiResponse<>(true, "소개글 업데이트에 성공했습니다.");
    }

    // 내 개인정보 조회
    @GetMapping("/personal-profile/{user_id}")
    public ApiResponse<?> getPersonalProfile(@PathVariable("user_id") Long userId, Authentication authentication) {
        // 해당 사용자 찾은 후 본인 확인 수행
        User user = userService.getUserById(userId);
        verifyAuthenticatedUser(authentication, user);

        UserDetailResponseDto userDetailResponseDto = UserDetailResponseDto.fromEntity(user);
        return new ApiResponse<>(true, "사용자 개인 정보 조회에 성공했습니다.", userDetailResponseDto);
    }

    // 닉네임 변경
    @PutMapping("/nickname")
    public ApiResponse<?> changeNickname(@Valid @RequestBody NicknameRequestDto dto, Authentication authentication) {
        // 해당 사용자 찾은 후 본인 확인 수행
        User user = userService.getUserById(dto.getUserId());
        verifyAuthenticatedUser(authentication, user);

        userService.updateNickname(user, dto.getNewNickname());
        return new ApiResponse<>(true, "닉네임 변경을 완료했습니다.");
    }

    // 비밀번호 변경 - UserController 에도 있지만, 마이페이지에서 기존 비밀번호를 알아야 가능한 변경
    @PutMapping("/password")
    public ApiResponse<?> changePassword(@Valid @RequestBody PasswordUpdateRequestDto dto, Authentication authentication) {
        // 해당 사용자 찾은 후 본인 확인 수행
        User user = userService.getUserById(dto.getUserId());
        verifyAuthenticatedUser(authentication, user);

        userService.updatePassword(user, dto, passwordEncoder);
        return new ApiResponse<>(true, "비밀번호 변경을 완료했습니다.");
    }

    // 회원 탈퇴 - 모든 정보를 삭제
    @DeleteMapping("/{user_id}/withdraw")
    public ApiResponse<?> removeAllUserInformation(@PathVariable("user_id") Long userId, Authentication authentication,
                                                   HttpServletRequest request, HttpServletResponse response) {
        try {
            // 해당 사용자 찾은 후 본인 확인 수행
            User user = userService.getUserById(userId);
            verifyAuthenticatedUser(authentication, user);

            // 회원 탈퇴 작업 수행
            userWithdrawalService.withdrawUser(user);
            jwtTokenizer.removeRefreshTokenCookie(request, response);

            return new ApiResponse<>(true, "회원 탈퇴가 완료되었습니다. 그동안 저희 서비스를 이용해 주셔서 감사합니다.");
        } catch (Exception e) {
            log.error("회원 탈퇴 실패", e);
            return new ApiResponse<>(false, "회원 탈퇴 중 오류가 발생했습니다. 다시 시도해 주세요.");
        }
    }

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

    // 팔로워 조회
    @GetMapping("/{user_id}/follower")
    public ApiResponse<?> getFollower(@PathVariable("user_id") Long userId,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "15") int size) {
        PagedResponseDto<FollowResponseDto> followerList = followService.getFollower(userId, page, size);
        return new ApiResponse<>(true, "팔로워 사용자 조회에 성공했습니다.", followerList);
    }

    // 팔로잉 조회
    @GetMapping("/{user_id}/following")
    public ApiResponse<?> getFollowing(@PathVariable("user_id") Long userId,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "15") int size) {
        PagedResponseDto<FollowResponseDto> followingList = followService.getFollowing(userId, page, size);
        return new ApiResponse<>(true, "팔로잉 사용자 조회에 성공했습니다.", followingList);
    }

    // 내 여행 전체 조회
    @GetMapping("/{user_id}/my-trip")
    public ApiResponse<?> getAllPlans(@PathVariable("user_id") Long userId, Authentication authentication) {
        // 해당 사용자 찾은 후 본인 확인 수행
        User user = userService.getUserById(userId);
        verifyAuthenticatedUser(authentication, user);

        List<PlanListResponseDto> planList = planUserService.getAllPlansByUserAndStatus(userId);
        return new ApiResponse<>(true, "전체 여행 조회에 성공했습니다.", planList);
    }

    // 사용자가 모집 중인 플랜 전체 조회
    @GetMapping("/{user_id}/my-recruitment")
    public ApiResponse<?> getAllRecruitmentPlans(@PathVariable("user_id") Long userId, Authentication authentication) {
        // 해당 사용자 찾은 후 본인 확인 수행
        User user = userService.getUserById(userId);
        verifyAuthenticatedUser(authentication, user);

        List<PlanListResponseDto> planList = planUserService.getAllRecruitmentPlans(userId);
        return new ApiResponse<>(true, "전체 여행 조회에 성공했습니다.", planList);
    }

    // 북마크 플랜 전체 조회
    @GetMapping("/{user_id}/my-bookmark")
    public ApiResponse<?> getAllBookmarkedPlans(@PathVariable("user_id") Long userId, Authentication authentication) {
        // 해당 사용자 찾은 후 본인 확인 수행
        User user = userService.getUserById(userId);
        verifyAuthenticatedUser(authentication, user);

        List<PlanListResponseDto> planList = planService.getAllBookmarkedPlans(user);
        return new ApiResponse<>(true, "북마크한 여행 조회에 성공했습니다.", planList);
    }

    // 좋아요한 플랜 전체 조회
    @GetMapping("/{user_id}/my-like")
    public ApiResponse<?> getAllLikedPlans(@PathVariable("user_id") Long userId, Authentication authentication) {
        // 해당 사용자 찾은 후 본인 확인 수행
        User user = userService.getUserById(userId);
        verifyAuthenticatedUser(authentication, user);

        List<PlanListResponseDto> planList = planService.getAllLikedPlans(user);
        return new ApiResponse<>(true, "좋아요한 여행 조회에 성공했습니다.", planList);
    }

    // 댓글 단 플랜 전체 조회
    @GetMapping("/{user_id}/my-comment")
    public ApiResponse<?> getAllCommentedPlans(@PathVariable("user_id") Long userId, Authentication authentication) {
        // 해당 사용자 찾은 후 본인 확인 수행
        User user = userService.getUserById(userId);
        verifyAuthenticatedUser(authentication, user);

        List<CommentSummaryResponseDto> planList = commentService.getAllCommentedPlansByUser(user);
        return new ApiResponse<>(true, "댓글을 단 여행 조회에 성공했습니다.", planList);
    }

    // 프로필 이미지
    @PostMapping("/profile-image/{userId}")
    public ApiResponse<UserProfileImageRequestDTO> uploadProfileImage(@PathVariable("userId") Long userId,
                                                                      @RequestParam("file") MultipartFile file) {
        try {
            userService.updateProfileImage(userId, file);

            User user = userService.getProfileImageUrl(userId);
            String profileImageUrl = user.getProfileImageUrl();

            UserProfileImageRequestDTO response = new UserProfileImageRequestDTO(userId, profileImageUrl);
            return new ApiResponse<>(true, "프로필 이미지가 성공적으로 업로드 되었습니다.", response);
        } catch (Exception e) {
            return new ApiResponse<>(false, "프로필 이미지 업로드 실패했습니다.");
        }
    }

    // 본인 확인 메소드
    private void verifyAuthenticatedUser(Authentication authentication, User user) {
        if (!authenticationUtil.isAuthenticatedUser(authentication, user)) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
    }
}
