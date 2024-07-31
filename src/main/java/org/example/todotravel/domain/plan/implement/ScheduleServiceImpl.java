package org.example.todotravel.domain.plan.implement;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.dto.request.ScheduleCreateRequestDto;
import org.example.todotravel.domain.plan.dto.response.ScheduleResponseDto;
import org.example.todotravel.domain.plan.entity.Location;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.entity.Schedule;
import org.example.todotravel.domain.plan.repository.ScheduleRepository;
import org.example.todotravel.domain.plan.service.ScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final LocationServiceImpl locationService;
    private final PlanServiceImpl planService;

    //여행 일정 찾기
    @Override
    @Transactional(readOnly = true)
    public Schedule findByScheduleId(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다."));
    }

    //여행 일정 responseDto로 전달
    @Override
    @Transactional(readOnly = true)
    public ScheduleResponseDto getSchedule(Long scheduleId) {
        return ScheduleResponseDto.fromEntity(findByScheduleId(scheduleId));
    }

    //여행 일정 생성하기
    @Override
    @Transactional
    public Schedule createSchedule(Long planId, ScheduleCreateRequestDto dto) {
        Plan plan = planService.getPlan(planId);

        Location location = locationService.findByLocationId(dto.getLocationId())
                .orElseThrow(() -> new RuntimeException("장소를 찾을 수 없습니다."));

        Schedule schedule = Schedule.builder()
                .status(false)
                .travelDayCount(dto.getTravelDayCount())
                .description(dto.getDescription())
                .plan(plan)
                .location(location)
//                .vehicle(dto.getVehicleId())
//                .budget(dto.getBudgetId())
                .build();
        return scheduleRepository.save(schedule);
    }

    //여행 일정 삭제하기
    @Override
    @Transactional
    public void destroySchedule(Long scheduleId) {
        scheduleRepository.delete(findByScheduleId(scheduleId));
    }

    //여행 일정 수정하기 - status
    @Override
    @Transactional
    public Schedule updateStatus(Long scheduleId) {
        Schedule schedule = findByScheduleId(scheduleId);
        schedule.setStatus(!schedule.getStatus());
        return scheduleRepository.save(schedule);
    }
}
