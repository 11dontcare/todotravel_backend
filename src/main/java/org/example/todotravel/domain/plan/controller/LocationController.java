package org.example.todotravel.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.dto.request.LocationCreateRequestDto;
import org.example.todotravel.domain.plan.entity.Location;
import org.example.todotravel.domain.plan.service.implement.LocationServiceImpl;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/location")
public class LocationController {
    private final LocationServiceImpl locationService;

    //여행 장소 저장하기
    @PostMapping
    public ApiResponse<Location> insertLocation(@Valid @RequestBody LocationCreateRequestDto dto) {
        Location location = locationService.createLocation(
                dto.getName(), dto.getLatitude(), dto.getLongitude()
        );
        return new ApiResponse<>(true, "장소 저장 성공", location);
    }
}
