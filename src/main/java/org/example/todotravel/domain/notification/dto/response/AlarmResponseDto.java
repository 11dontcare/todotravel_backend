package org.example.todotravel.domain.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.todotravel.domain.notification.entity.Alarm;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AlarmResponseDto {
    Long alarmId;
    Long userId;
    String alarmContent;
    LocalDateTime createdDate;
    Boolean status;

    public static AlarmResponseDto fromEntity(Alarm alarm) {
        return AlarmResponseDto.builder()
                .alarmId(alarm.getAlarmId())
                .userId(alarm.getAlarmUser().getUserId())
                .alarmContent(alarm.getAlarmContent())
                .createdDate(alarm.getCreatedDate())
                .status(alarm.getStatus())
                .build();
    }
}
