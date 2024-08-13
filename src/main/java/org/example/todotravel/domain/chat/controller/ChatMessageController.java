package org.example.todotravel.domain.chat.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.chat.dto.request.ChatMessageRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatMessageResponseDto;
import org.example.todotravel.domain.chat.repository.ChatMessageRepository;
import org.example.todotravel.domain.chat.service.ChatMessageService;
import org.example.todotravel.domain.chat.service.impl.ChatMessageServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final SimpMessageSendingOperations template;

    // 메시지 전송
    @MessageMapping("/message")
    public Mono<ResponseEntity<Void>> receiveMessage(@RequestBody ChatMessageRequestDto dto) {
        return chatMessageService.saveChatMessage(dto).flatMap(message -> {
            // 메시지를 해당 채팅방 구독자들에게 전송
            template.convertAndSend("/sub/chatroom/" + dto.getRoomId(),
                ChatMessageResponseDto.of(message));
            return Mono.just(ResponseEntity.ok().build());
        });
    }

}
