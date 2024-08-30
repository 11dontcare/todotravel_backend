package org.example.todotravel.domain.notification.entity;

import lombok.*;

import jakarta.persistence.*;
import org.example.todotravel.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "alarms")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id", nullable = false)
    private Long alarmId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User alarmUser;

    @Column(name = "alarm_content", nullable = false, length = 255)
    private String alarmContent;

    @Column(name = "created_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDate;

    @Column(name = "status", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean status;

    // @PrePersist 메서드를 추가하여 기본값 설정
    @PrePersist
    protected void onCreate() {
        // status 필드가 null인 경우, 기본값으로 false 설정
        if (this.status == null) {
            this.status = Boolean.FALSE;
        }

        // createdDate 필드가 null인 경우, 현재 시간으로 설정
        if (this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }
    }
}
