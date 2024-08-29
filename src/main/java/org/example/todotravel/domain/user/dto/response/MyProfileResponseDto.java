package org.example.todotravel.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.todotravel.domain.plan.dto.response.CommentSummaryResponseDto;
import org.example.todotravel.domain.plan.dto.response.PlanListResponseDto;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MyProfileResponseDto extends UserProfileResponseDto {
    private List<PlanListResponseDto> recruitingPlans;
    private List<PlanListResponseDto> recentBookmarks;
    private List<PlanListResponseDto> recentLikes;
    private List<CommentSummaryResponseDto> recentComments;

    public static MyProfileResponseDto from(UserProfileResponseDto userProfileResponseDto,
                                            List<PlanListResponseDto> recruitingPlans,
                                            List<PlanListResponseDto> recentBookmarks,
                                            List<PlanListResponseDto> recentLikes,
                                            List<CommentSummaryResponseDto> recentComments) {
        return MyProfileResponseDto.builder()
            .userId(userProfileResponseDto.getUserId())
            .nickname(userProfileResponseDto.getNickname())
            .gender(userProfileResponseDto.getGender())
            .age(userProfileResponseDto.getAge())
            .info(userProfileResponseDto.getInfo())
            .profileImageUrl(userProfileResponseDto.getProfileImageUrl())
            .isFollowing(userProfileResponseDto.isFollowing())
            .followerCount(userProfileResponseDto.getFollowerCount())
            .followingCount(userProfileResponseDto.getFollowingCount())
            .planCount(userProfileResponseDto.getPlanCount())
            .planList(userProfileResponseDto.getPlanList())
            .recruitingPlans(recruitingPlans)
            .recentBookmarks(recentBookmarks)
            .recentLikes(recentLikes)
            .recentComments(recentComments)
            .build();
    }
}
