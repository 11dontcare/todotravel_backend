package org.example.todotravel.domain.plan.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ScheduleRequestDto {
    @NotNull
    private Long locationId;
    @NotBlank
    private Integer travelDayCount;

    private String description;
    //null 허용
    private Long vehicleId;
    private Long budgetId;
}