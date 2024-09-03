package org.example.todotravel.domain.plan.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.dto.request.ScheduleCreateRequestDto;
import org.example.todotravel.domain.plan.dto.response.ScheduleResponseDto;
import org.example.todotravel.domain.plan.entity.Schedule;
import org.example.todotravel.domain.plan.service.ScheduleService;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plan")
public class ScheduleController {
    private final ScheduleService scheduleService;

    //여행 일정 생성
    @PostMapping("/{plan_id}/course")
    public ApiResponse<ScheduleResponseDto> createSchedule(@PathVariable("plan_id") Long planId,
                                                           @Valid @RequestBody ScheduleCreateRequestDto dto) {
        Schedule schedule = scheduleService.createSchedule(planId, dto);
        ScheduleResponseDto responseDto = ScheduleResponseDto.fromEntity(schedule);
        return new ApiResponse<>(true, "일정 저장 성공", responseDto);
    }

    //여행 일정 삭제
    @DeleteMapping("/{plan_id}/course/{schedule_id}")
    public ApiResponse<Void> removeSchedule(@PathVariable("plan_id") Long planId,
                                            @PathVariable("schedule_id") Long scheduleId) {
        scheduleService.removeSchedule(scheduleId);
        return new ApiResponse<>(true, "일정이 삭제되었습니다.");
    }

    //여행 일정 불러오기
    @GetMapping("/{plan_id}/course/{schedule_id}")
    public ApiResponse<ScheduleResponseDto> getSchedule(@PathVariable("plan_id") Long planId,
                                                        @PathVariable("schedule_id") Long scheduleId) {
        ScheduleResponseDto responseDto = scheduleService.getSchedule(scheduleId);
        return new ApiResponse<>(true, "일정 불러오기", responseDto);
    }

    //여행 일정 description 관리 - 수정
    @PutMapping("/{schedule_id}/description")
    public ApiResponse<String> updateScheduleDescription(
            @PathVariable("schedule_id") Long scheduleId,
            @RequestBody String description) {
        scheduleService.updateDescription(scheduleId, description);
        return new ApiResponse<>(true, "메모가 수정되었습니다..", description);
    }

    //여행 일정 status 관리
    @PutMapping("/{schedule_id}/status")
    public ApiResponse<Void> updateScheduleStatus(@PathVariable("schedule_id") Long scheduleId) {
        scheduleService.updateStatus(scheduleId);
        return new ApiResponse<>(true, "여행 일정 상태 저장 완료");
    }

    //여행 일정 vehicle 관리 - 수정(등록)
    @PutMapping("/{schedule_id}/vehicle")
    public ApiResponse<Schedule.VehicleType> updateScheduleVehicle(
            @PathVariable("schedule_id") Long scheduleId,
            @RequestBody Schedule.VehicleType vehicle) {
        scheduleService.updateVehicle(scheduleId, vehicle);
        return new ApiResponse<>(true, "이동수단이 추가되었습니다.", vehicle);
    }

    //여행 일정 price 관리 - 수정(등록)
    @PutMapping("/{schedule_id}/price")
    public ApiResponse<Long> updateSchedulePrice(@PathVariable("schedule_id") Long scheduleId,
                                                 @RequestBody Long price) {
        System.out.println("Received price: " + price);
        scheduleService.updatePrice(scheduleId, price);
        return new ApiResponse<>(true, "예산이 추가되었습니다.", price);
    }

}
