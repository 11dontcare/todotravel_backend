package org.example.todotravel.domain.chat.repository;

import org.example.todotravel.domain.chat.entity.ChatRoom;
import org.example.todotravel.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByPlanPlanId(Long planId);
    List<ChatRoom> findByPlanIn(List<Plan> plans);

    @Modifying
    @Query("DELETE FROM ChatRoom cr WHERE cr.roomId = :roomId")
    void deleteByRoomId(@Param("roomId") Long roomId);
}
