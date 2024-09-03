package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.entity.Location;

import java.util.Optional;

public interface LocationService {
    Optional<Location> getByLocationPoint(double latitude, double longitude);
    Location getByLocationId(Long locationId);
    Location addLocation(String name, double latitude, double longitude);
}
