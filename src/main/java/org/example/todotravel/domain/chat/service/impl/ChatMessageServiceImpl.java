package org.example.todotravel.domain.chat.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

private final ChatMessageRepository chatMessageRepository;
private final ChatRoomUserRepository chatRoomUserRepository;

//     이전 채팅 내용 조회
    @Transactional
    public Flux<ChatMessageResponseDto> findChatMessages(Long roomId) {
        Flux<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomId(roomId);
        return chatMessages.map(ChatMessageResponseDto::of);
    }


//    @Transactional
//    public Mono<ChatMessage> saveChatMessage(ChatMessageRequestDto dto) {
//        return chatMessageRepository.save(
//            new ChatMessage(dto.getUserId(), dto.getRoomId(), dto.getName(), dto.getContent()));
//    }

//    @Transactional
//    public Mono<ChatMessage> saveChatMessage(ChatMessageRequestDto dto) {
//        ChatMessage chatMessage = new ChatMessage(dto.getUserId(), dto.getRoomId(), dto.getName(), dto.getContent());
//        chatMessage.setCreateAt(LocalDateTime.now());
//        return chatMessageRepository.save(chatMessage);
//    }

    @Transactional
    public Mono<ChatMessage> saveChatMessage(ChatMessageRequestDto dto) {
        User user = new User();
        user.setUserId(dto.getUserId());

        Optional<ChatRoomUser> chatRoomUserOpt = chatRoomUserRepository.findByUser(user);

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
