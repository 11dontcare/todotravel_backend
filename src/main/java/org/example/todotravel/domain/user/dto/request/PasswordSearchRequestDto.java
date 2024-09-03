package org.example.todotravel.domain.user.dto.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PasswordSearchRequestDto {
    private String name;
    private LocalDate birthDate;
    private String email;
}
