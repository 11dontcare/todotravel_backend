package org.example.todotravel.domain.plan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ScheduleRequestDto {
    private Long locationId;
    private Integer travelDayCount;
    private String description;
    //null 허용
    private Long vehicleId;
    private Long budgetId;
}