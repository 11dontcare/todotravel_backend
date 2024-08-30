package org.example.todotravel.domain.plan.dto.request;

import lombok.*;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PlanRequestDto {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String frontLocation;
    private String location;
    private Boolean isPublic;
    private Boolean status;
    private Long totalBudget;
    private String planThumbnailUrl;

    public Plan toEntity(){
        return Plan.builder()
                .title(title)
                .location(location)
                .frontLocation(frontLocation)
                .startDate(startDate)
                .endDate(endDate)
                .isPublic(isPublic)
                .status(status)
                .totalBudget(totalBudget)
                .planThumbnailUrl(planThumbnailUrl)
                .build();
    }
}