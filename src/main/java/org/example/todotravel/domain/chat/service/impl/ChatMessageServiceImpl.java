package org.example.todotravel.domain.chat.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.chat.dto.request.ChatMessageRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatMessageResponseDto;
import org.example.todotravel.domain.chat.entity.ChatMessage;
import org.example.todotravel.domain.chat.entity.ChatRoomUser;
import org.example.todotravel.domain.chat.repository.ChatMessageRepository;
import org.example.todotravel.domain.chat.repository.ChatRoomUserRepository;
import org.example.todotravel.domain.chat.service.ChatMessageService;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

private final ChatMessageRepository chatMessageRepository;
private final ChatRoomUserRepository chatRoomUserRepository;
private final UserRepository userRepository;

//     이전 채팅 내용 조회
    @Transactional
    public Flux<ChatMessageResponseDto> findChatMessages(Long roomId) {
        Flux<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomId(roomId);
        return chatMessages.map(ChatMessageResponseDto::of);
    }

// 메시지 저장
    @Transactional
    public Mono<ChatMessage> saveChatMessage(ChatMessageRequestDto dto) {
        Long userId = dto.getUserId();
        Long roomId = dto.getRoomId();

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Optional<ChatRoomUser> chatRoomUserOpt = chatRoomUserRepository.findByUserAndChatRoomRoomId(user, roomId);

        return Mono.justOrEmpty(chatRoomUserOpt)
            .flatMap(chatRoomUser -> {
                ChatMessage chatMessage = new ChatMessage(
                    chatRoomUser.getUser().getUserId(),
                    chatRoomUser.getChatRoom().getRoomId(),
                    chatRoomUser.getUser().getNickname(),
                    dto.getContent()
                );
                return chatMessageRepository.save(chatMessage);
            });
    }
}
