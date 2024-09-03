package org.example.todotravel.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.dto.request.LocationRequestDto;
import org.example.todotravel.domain.plan.entity.Location;
import org.example.todotravel.domain.plan.service.LocationService;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/location")
public class LocationController {
    private final LocationService locationService;

    //여행 장소 저장하기
    @PostMapping
    public ApiResponse<Location> addLocation(@Valid @RequestBody LocationRequestDto dto) {
        Location location = locationService.addLocation(
                dto.getName(), dto.getLatitude(), dto.getLongitude()
        );
        return new ApiResponse<>(true, "장소 저장 성공", location);
    }

    //여행 장소 검색하기
    @GetMapping("/{location_id}")
    public ApiResponse<Location> getByLocationId(@PathVariable("location_id") Long locationId) {
        Location location = locationService.getByLocationId(locationId);
        return new ApiResponse<>(true, "장소 검색 성공", location);
    }
}
