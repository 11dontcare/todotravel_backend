package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.dto.request.ScheduleCreateRequestDto;
import org.example.todotravel.domain.plan.dto.response.ScheduleResponseDto;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.entity.Schedule;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ScheduleService {
    //repository 접근
    Schedule findByScheduleId(Long scheduleId);

    //비즈니스 로직 처리
    Schedule createSchedule(Long planId, ScheduleCreateRequestDto dto);
    void destroySchedule(Long scheduleId);
    ScheduleResponseDto getSchedule(Long scheduleId);
    Schedule updateDescription(Long scheduleId, String description);
    Schedule updateStatus(Long scheduleId);
    Schedule updateVehicle(Long scheduleId, Schedule.VehicleType vehicle);
    Schedule updatePrice(Long scheduleId, Long price);

    //플랜 상세 조회 - 김민정
    List<ScheduleResponseDto> getSchedulesByPlan(Long planId);

    void deleteAllSchedulesByPlan(Plan plan);
}
