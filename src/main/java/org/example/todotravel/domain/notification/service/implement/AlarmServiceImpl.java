package org.example.todotravel.domain.notification.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.notification.dto.request.AlarmRequestDto;
import org.example.todotravel.domain.notification.entity.Alarm;
import org.example.todotravel.domain.notification.repository.AlarmRepository;
import org.example.todotravel.domain.notification.service.AlarmService;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.UserService;
import org.example.todotravel.global.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {
    private final AlarmRepository alarmRepository;
    private final UserService userService;

    //모든 알림 찾기 - userId
    @Override
    @Transactional(readOnly = true)
    public List<Alarm> findByUserId(Long userId) {
        return alarmRepository.findByAlarmUser_UserId(userId);
    }

    //알림 찾기 - alarmId
    @Override
    @Transactional(readOnly = true)
    public Alarm findByAlarmId(Long alarmId) {
        return alarmRepository.findById(alarmId)
                .orElseThrow(() -> new RuntimeException("알림을 찾을 수 없습니다."));
    }

    //알림 생성하기
    @Override
    @Transactional
    public Alarm createAlarm(AlarmRequestDto dto) {
        User user = userService.getUserByUserId(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));

        Alarm alarm = Alarm.builder()
                .alarmUser(user)
                .alarmContent(dto.getAlarmContent())
                .createdDate(LocalDateTime.now())
                .status(false)
                .build();
        return alarmRepository.save(alarm);
    }

    //알림 수정하기 (읽음)
    @Override
    @Transactional
    public void updateAlarm(Long alarmId) {
        Alarm alarm = findByAlarmId(alarmId);
        alarm.setStatus(true);
        alarmRepository.save(alarm);
    }

    //모든 알림 수정하기 (읽음)
    @Override
    @Transactional
    public void updateAllAlarm(Long userId) {
        alarmRepository.markAllAlarmsAsReadByUserId(userId);
    }

    //알림 삭제하기
    @Override
    @Transactional
    public void deleteAlarm(Long alarmId) {
        alarmRepository.deleteById(alarmId);
    }

    //모든 알림 삭제하기
    @Override
    @Transactional
    public void deleteAllAlarm(Long userId) {
        List<Alarm> alarmList = findByUserId(userId);
        alarmRepository.deleteAll(alarmList);
    }
}
