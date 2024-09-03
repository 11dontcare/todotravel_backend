package org.example.todotravel.domain.notification.service;

import org.example.todotravel.domain.notification.dto.request.AlarmRequestDto;
import org.example.todotravel.domain.notification.entity.Alarm;

import java.util.List;

public interface AlarmService {
    Alarm createAlarm(AlarmRequestDto dto);
    List<Alarm> getByUserId(Long userId);
    Alarm getByAlarmId(Long alarmId);
    void updateAlarm(Long alarmId);
    void updateAllAlarm(Long userId);
    void removeAlarm(Long alarmId);
    void removeAllAlarm(Long userId);
}
