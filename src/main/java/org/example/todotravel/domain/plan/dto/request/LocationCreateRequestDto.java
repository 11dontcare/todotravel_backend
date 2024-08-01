package org.example.todotravel.domain.plan.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class LocationCreateRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private double latitude;
    @NotBlank
    private double longitude;
}
