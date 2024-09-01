package org.example.todotravel.domain.notification.repository;

import org.example.todotravel.domain.notification.entity.Alarm;
import org.example.todotravel.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    // 기본적으로 사용자의 모든 알림 조회
    List<Alarm> findByAlarmUser_UserId(Long userId);

    @Modifying
    @Query("UPDATE Alarm a SET a.status = true WHERE a.alarmUser.userId = :userId")
    int markAllAlarmsAsReadByUserId(@Param("userId") Long userId);

//    // 읽지 않은 알림만 조회
//    @Query("SELECT a FROM Alarm a WHERE a.alarmUser.userId = :userId AND a.status = false")
//    List<Alarm> findUnreadAlarmsByUserId(@Param("userId") Long userId);
//
//    // 특정 시간 이후의 알림 조회
//    @Query("SELECT a FROM Alarm a WHERE a.alarmUser.userId = :userId AND a.createdDate > :since")
//    List<Alarm> findAlarmsByUserIdSince(@Param("userId") Long userId, @Param("since") LocalDateTime since);
//
//    // 읽지 않은 알림의 수 조회 (Native Query 사용)
//    @Query(value = "SELECT COUNT(*) FROM alarms a WHERE a.user_id = :userId AND a.status = false", nativeQuery = true)
//    int countUnreadAlarmsByUserId(@Param("userId") Long userId);
}
