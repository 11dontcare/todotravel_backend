package org.example.todotravel.domain.plan.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ScheduleCreateRequestDto {
    @NotNull
    private Long locationId;
    @NotNull
    private Integer travelDayCount;
    //null 허용
    private String description;
    private LocalTime travelTime;
}