package org.example.todotravel.domain.plan.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VehicleRequestDto {
    @NotBlank
    private String vehicle;
}
