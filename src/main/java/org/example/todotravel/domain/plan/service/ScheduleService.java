package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.dto.request.ScheduleRequestDto;
import org.example.todotravel.domain.plan.entity.Schedule;
import org.springframework.stereotype.Service;

@Service
public interface ScheduleService {

    //비즈니스 로직 처리
    Schedule createSchedule(Long planId, ScheduleRequestDto dto);
}
