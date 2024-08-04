package org.example.todotravel.domain.chat.repository;

import org.example.todotravel.domain.chat.entity.ChatRoomDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomDetailRepository extends JpaRepository<ChatRoomDetail, Long> {
}
