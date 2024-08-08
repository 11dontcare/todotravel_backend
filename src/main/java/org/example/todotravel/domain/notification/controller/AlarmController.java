package org.example.todotravel.domain.notification.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.notification.dto.response.AlarmResponseDto;
import org.example.todotravel.domain.notification.entity.Alarm;
import org.example.todotravel.domain.notification.service.implement.AlarmServiceImpl;
import org.example.todotravel.domain.plan.dto.response.ScheduleResponseDto;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class AlarmController {
    final private AlarmServiceImpl alarmService;

    //사용자에 대한 모든 알림 가져오기
    @GetMapping("/{user_id}")
    public ApiResponse<List<AlarmResponseDto>> showAllAlarms(@PathVariable("user_id") Long userId) {
        List<Alarm> alarmList = alarmService.findByUserId(userId);
        List<AlarmResponseDto> responseDtoList = alarmList.stream()
                .map(AlarmResponseDto::fromEntity)
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "모든 알림 가져오기 성공", responseDtoList);
    }

    //알림 수정하기 (읽음)
    @PutMapping("/read/{alarm_id}")
    public ApiResponse<Void> updateAlarm(@PathVariable("alarm_id") Long alarmId) {
        alarmService.updateAlarm(alarmId);
        return new ApiResponse<>(true,"알림 읽음 상태 수정 완료");
    }

    //모든 알림 수정하기 (읽음)
    @PutMapping("/readAll/{user_id}")
    public ApiResponse<Void> updateAllAlarm(@PathVariable("user_id") Long userId) {
        alarmService.updateAllAlarm(userId);
        return new ApiResponse<>(true,"모든 알림 읽음 상태 수정 완료");
    }


    //알림 삭제하기
    @DeleteMapping("/delete/{alarm_id}")
    public ApiResponse<Void> deleteAlarm(@PathVariable("alarm_id") Long alarmId) {
        alarmService.deleteAlarm(alarmId);
        return new ApiResponse<>(true,"알림 삭제 완료");
    }

    //모든 알림 삭제하기
    @DeleteMapping("/deleteAll/{user_id}")
    public ApiResponse<Void> deleteAllAlarm(@PathVariable("user_id") Long userId) {
        alarmService.deleteAllAlarm(userId);
        return new ApiResponse<>(true,"모든 알림 삭제 완료");
    }
}
