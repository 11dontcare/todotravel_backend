package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.dto.request.ScheduleCreateRequestDto;
import org.example.todotravel.domain.plan.dto.response.ScheduleResponseDto;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.entity.Schedule;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ScheduleService {

    Schedule getByScheduleId(Long scheduleId);
    Schedule createSchedule(Long planId, ScheduleCreateRequestDto dto);
    void removeSchedule(Long scheduleId);
    ScheduleResponseDto getSchedule(Long scheduleId);
    Schedule updateDescription(Long scheduleId, String description);
    Schedule updateStatus(Long scheduleId);
    Schedule updateVehicle(Long scheduleId, Schedule.VehicleType vehicle);
    Schedule updatePrice(Long scheduleId, Long price);
    List<ScheduleResponseDto> getSchedulesByPlan(Long planId);
    void removeAllSchedulesByPlan(Plan plan);
}
