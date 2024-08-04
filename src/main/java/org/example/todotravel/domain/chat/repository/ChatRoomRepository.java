package org.example.todotravel.domain.chat.repository;

import org.example.todotravel.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
