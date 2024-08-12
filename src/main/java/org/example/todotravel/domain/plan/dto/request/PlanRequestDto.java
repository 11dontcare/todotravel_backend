package org.example.todotravel.domain.plan.dto.request;

import lombok.*;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlanRequestDto {
    private String title;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isPublic;
    private Boolean status;
    private Long totalBudget;

    public Plan toEntity(){
        return Plan.builder()
                .title(title)
                .location(location)
                .startDate(startDate)
                .endDate(endDate)
                .isPublic(isPublic)
                .status(status)
                .totalBudget(totalBudget)
                .build();
    }
}