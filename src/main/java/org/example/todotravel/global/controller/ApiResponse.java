package org.example.todotravel.global.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ApiResponse<T> { //제너릭 타입 T
    private boolean success;
    private String message;

    //data가 null인 경우 직렬화하지 않음
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    //성공메시지만 전달할 경우
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    //성공메시지 및 필요한 Data 같이 전달할 경우
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}