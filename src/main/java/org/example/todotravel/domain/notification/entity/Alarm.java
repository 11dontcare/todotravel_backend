package org.example.todotravel.domain.notification.entity;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alarms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id", nullable = false)
    private Long alarmId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "alarm_content", nullable = false, length = 255)
    private String alarmContent;

    @Column(name = "created_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDate;

    @Column(name = "status", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean status;
}
