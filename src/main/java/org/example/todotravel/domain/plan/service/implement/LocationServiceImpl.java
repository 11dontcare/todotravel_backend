package org.example.todotravel.domain.plan.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.entity.Location;
import org.example.todotravel.domain.plan.repository.LocationRepository;
import org.example.todotravel.domain.plan.service.LocationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    //위치 찾기 - 좌표
    @Override
    @Transactional(readOnly = true)
    public Optional<Location> getByLocationPoint(double latitude, double longitude) {
        return locationRepository.findByLatitudeAndLongitude(latitude, longitude);
    }

    //위치 찾기 - id
    @Override
    @Transactional(readOnly = true)
    public Location getByLocationId(Long locationId) {
        return locationRepository.findById(locationId).orElseThrow(() -> new RuntimeException("장소를 찾을 수 없습니다."));
    }

    //위치 저장하기
    @Override
    @Transactional
    public Location addLocation(String name, double latitude, double longitude) {
        //이미 존재하는 경우 기존의 장소 전달
        Optional<Location> existingLocation = getByLocationPoint(latitude, longitude);
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
