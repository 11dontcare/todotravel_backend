package org.example.todotravel.domain.plan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.todotravel.domain.plan.entity.Schedule;

import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ScheduleResponseDto {
    private Long scheduleId;
    private Boolean status;
    private Integer travelDayCount;
    private String description;
    private LocalTime travelTime;
    private String vehicle;
    private Long planId;
    private Long locationId;
    private Long budgetId;


    public static ScheduleResponseDto fromEntity(Schedule schedule) {
        return ScheduleResponseDto.builder()
                .scheduleId(schedule.getScheduleId())
                .status(schedule.getStatus())
                .travelDayCount(schedule.getTravelDayCount())
                .description(schedule.getDescription())
                .travelTime(schedule.getTravelTime())
                .planId(schedule.getPlan().getPlanId())
                .locationId(schedule.getLocation().getLocationId())
                .vehicle(schedule.getVehicle() != null ? schedule.getVehicle().name() : null)
                .budgetId(schedule.getBudget() != null ? schedule.getBudget().getBudgetId() : null)
                .build();
    }
}