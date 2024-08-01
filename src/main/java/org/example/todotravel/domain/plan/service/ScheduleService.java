package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.dto.request.ScheduleCreateRequestDto;
import org.example.todotravel.domain.plan.dto.response.ScheduleResponseDto;
import org.example.todotravel.domain.plan.entity.Schedule;
import org.springframework.stereotype.Service;

@Service
public interface ScheduleService {
    //repository 접근
    Schedule findByScheduleId(Long scheduleId);

    //비즈니스 로직 처리
    Schedule createSchedule(Long planId, ScheduleCreateRequestDto dto);
    void destroySchedule(Long scheduleId);
    ScheduleResponseDto getSchedule(Long scheduleId);

    Schedule updateStatus(Long scheduleId);
    Schedule updateVehicle(Long scheduleId, String vehicle);
    Schedule deleteVehicle(Long scheduleId);
    Schedule updatePrice(Long scheduleId, Long price);
    Schedule deletePrice(Long scheduleId);
}
