package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.entity.Location;

import java.util.Optional;

public interface LocationService {
    //repository 접근
    Optional<Location> findByLocationPoint(double latitude, double longitude);
    public Location findByLocationId(Long locationId);

    //비즈니스 로직 처리
    Location createLocation(String name, double latitude, double longitude);
}
