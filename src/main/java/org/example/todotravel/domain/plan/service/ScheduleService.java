package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.dto.request.ScheduleRequestDto;
import org.example.todotravel.domain.plan.entity.Schedule;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ScheduleService {
    //repository 접근
    Optional<Schedule> findByScheduleId(Long scheduleId);

    //비즈니스 로직 처리
    Schedule createSchedule(Long planId, ScheduleRequestDto dto);
    void destroySchedule(Long scheduleId);
}
