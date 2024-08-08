package org.example.todotravel.domain.notification.repository;

import org.example.todotravel.domain.notification.entity.Alarm;
import org.example.todotravel.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByAlarmUser_UserId(Long userId);
}
