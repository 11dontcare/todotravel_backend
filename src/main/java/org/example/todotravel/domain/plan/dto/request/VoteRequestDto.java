package org.example.todotravel.domain.plan.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.todotravel.domain.plan.entity.Vote;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class VoteRequestDto {
    @NotNull
    private Long locationId;
    @NotNull
    private LocalDateTime endDate;
    @NotNull
    private Vote.Category category;
}
