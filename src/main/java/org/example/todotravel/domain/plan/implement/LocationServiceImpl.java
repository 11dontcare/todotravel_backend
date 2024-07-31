package org.example.todotravel.domain.plan.implement;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.entity.Location;
import org.example.todotravel.domain.plan.repository.LocationRepository;
import org.example.todotravel.domain.plan.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    @Autowired
    private final LocationRepository locationRepository;

    //위치 찾기 - 좌표
    @Override
    @Transactional(readOnly = true)
    public Optional<Location> findByLocationPoint(double latitude, double longitude) {
        return locationRepository.findByLatitudeAndLongitude(latitude, longitude);
    }

    //위치 찾기 - id
    @Override
    @Transactional(readOnly = true)
    public Optional<Location> findByLocationId(Long locationId) {
        return locationRepository.findById(locationId);
    }

    //위치 저장하기
    @Override
    @Transactional
    public Location saveLocation(String name, double latitude, double longitude) {
        //이미 존재하는 경우 기존의 장소 전달
        Optional<Location> existingLocation = findByLocationPoint(latitude, longitude);
        if (existingLocation.isPresent()) {
            return existingLocation.get();
        }
        //존재하지 않는 경우 새로 저장
        Location location = Location.builder()
                .name(name)
                .latitude(latitude)
                .longitude(longitude)
                .build();
        return locationRepository.save(location);
    }
}
