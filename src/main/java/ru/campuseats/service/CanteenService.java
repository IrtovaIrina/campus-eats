package ru.campuseats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.campuseats.dto.CanteenDto;
import ru.campuseats.dto.CreateCanteenRequestDto;
import ru.campuseats.dto.CreatePickupPointRequestDto;
import ru.campuseats.dto.PickupPointDto;
import ru.campuseats.entity.Canteen;
import ru.campuseats.entity.PickupPoint;
import ru.campuseats.entity.User;
import ru.campuseats.mapper.CanteenMapper;
import ru.campuseats.mapper.PickupPointMapper;
import ru.campuseats.repository.CanteenRepository;
import ru.campuseats.repository.PickupPointRepository;
import ru.campuseats.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CanteenService {

    private final CanteenRepository canteenRepository;
    private final PickupPointRepository pickupPointRepository;
    private final UserRepository userRepository;
    private final CanteenMapper canteenMapper;
    private final PickupPointMapper pickupPointMapper;

    @Transactional(readOnly = true)
    public List<CanteenDto> getAllCanteens(boolean onlyOpen) {
        List<Canteen> canteens = onlyOpen
                ? canteenRepository.findByIsOpenTrueOrderByRatingDesc()
                : canteenRepository.findAll();
        return canteenMapper.toDtoList(canteens);
    }

    @Transactional(readOnly = true)
    public CanteenDto getCanteenById(Long id) {
        Canteen canteen = findCanteenOrThrow(id);
        return canteenMapper.toDto(canteen);
    }

    @Transactional(readOnly = true)
    public List<PickupPointDto> getPickupPointsForCanteen(Long canteenId) {
        findCanteenOrThrow(canteenId);
        List<PickupPoint> points = pickupPointRepository.findByCanteenIdAndIsActiveTrueOrderByIdAsc(canteenId);
        return pickupPointMapper.toDtoList(points);
    }

    @Transactional
    public CanteenDto createCanteen(CreateCanteenRequestDto request) {
        Canteen canteen = canteenMapper.toEntity(request);
        if (request.getManagerId() != null) {
            User manager = userRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Менеджер с ID " + request.getManagerId() + " не найден"
                    ));
            canteen.setManager(manager);
        }
        return canteenMapper.toDto(canteenRepository.save(canteen));
    }

    @Transactional
    public PickupPointDto addPickupPoint(Long canteenId, CreatePickupPointRequestDto request) {
        Canteen canteen = findCanteenOrThrow(canteenId);
        PickupPoint pickupPoint = pickupPointMapper.toEntity(request);
        pickupPoint.setCanteen(canteen);
        return pickupPointMapper.toDto(pickupPointRepository.save(pickupPoint));
    }

    public Canteen findCanteenOrThrow(Long id) {
        return canteenRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Точка питания с ID " + id + " не найдена"
                ));
    }
}