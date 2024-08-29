package org.example.todotravel.domain.plan.dto.response;

import lombok.*;
import org.example.todotravel.domain.plan.entity.Vote;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class VoteResponseDto {
    private Long voteId;
    private Long locationId;
    private int voteCount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Vote.Category category;

    public static VoteResponseDto fromEntity(Vote vote) {
        return VoteResponseDto.builder()
                .voteId(vote.getVoteId())
                .locationId(vote.getLocation().getLocationId())
                .voteCount(vote.getVoteCount())
                .startDate(vote.getStartDate())
                .endDate(vote.getEndDate())
                .category(vote.getCategory())
                .build();
    }

}