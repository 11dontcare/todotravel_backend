package org.example.todotravel.domain.plan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class LocationRequestDto {
    private String name;
    private double latitude;
    private double longitude;
}
