package org.example.todotravel.domain.chat.repository;

import org.example.todotravel.domain.chat.entity.ChatRoom;
import org.example.todotravel.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // planId로 채팅방 찾기
    Optional<ChatRoom> findByPlanPlanId(Long planId);

    // 플랜 리스트에 해당하는 모든 채팅방 찾기
    List<ChatRoom> findByPlanIn(List<Plan> plans);

    // roomId로 채팅방 삭제
    @Modifying
    @Query("DELETE FROM ChatRoom cr WHERE cr.roomId = :roomId")
    void deleteByRoomId(@Param("roomId") Long roomId);

    // 메세지가 오갈 때, roomId로 roomDate 업데이트
    @Modifying
    @Query("UPDATE ChatRoom cr SET cr.roomDate = :roomDate WHERE cr.roomId = :roomId")
    int updateChatRoomByRoomId(@Param("roomId") Long roomId, @Param("roomDate")LocalDateTime roomDate);
}
