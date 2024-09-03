package org.example.todotravel.global.handler;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private String details;

    // 생성자
    public ErrorDetails(LocalDateTime timestamp, String message, String details) {
//        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }
}
