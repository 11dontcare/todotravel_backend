package org.example.todotravel.domain.notification.service;

import org.example.todotravel.domain.notification.dto.request.AlarmRequestDto;
import org.example.todotravel.domain.notification.entity.Alarm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AlarmService {
    List<Alarm> findByUserId(Long userId);
    Alarm findByAlarmId(Long alarmId);

    Alarm createAlarm(AlarmRequestDto dto);
    Alarm updateAlarm(Long alarmId);
    Alarm updateAllAlarm(Long userId);
    void deleteAlarm(Long alarmId);
    void deleteAllAlarm(Long userId);
}
