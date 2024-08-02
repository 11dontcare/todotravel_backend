package org.example.todotravel.domain.plan.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class LocationRequestDto {
    @NotBlank
    private String name;
    @NotNull
    private double latitude;
    @NotNull
    private double longitude;
}
