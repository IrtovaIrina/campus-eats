package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.campuseats.dto.CanteenDto;
import ru.campuseats.dto.CreateCanteenRequestDto;
import ru.campuseats.dto.CreatePickupPointRequestDto;
import ru.campuseats.dto.PickupPointDto;
import ru.campuseats.service.CanteenService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/canteens")
@RequiredArgsConstructor
@Tag(name = "02. Точки питания и Окна выдачи", description = "Выбор столовой кампуса и окна экспресс-выдачи заказов без очереди")
public class CanteenController {

    private final CanteenService canteenService;

    @GetMapping
    @Operation(summary = "Получить список всех точек питания кампуса (с сортировкой по рейтингу)")
    public List<CanteenDto> getAllCanteens(
            @RequestParam(defaultValue = "false") boolean onlyOpen
    ) {
        return canteenService.getAllCanteens(onlyOpen);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить подробную информацию о точке питания по ID")
    public CanteenDto getCanteenById(@PathVariable Long id) {
        return canteenService.getCanteenById(id);
    }

    @GetMapping("/{canteenId}/pickup-points")
    @Operation(summary = "Получить активные окна экспресс-выдачи для выбранной точки питания")
    public List<PickupPointDto> getPickupPoints(@PathVariable Long canteenId) {
        return canteenService.getPickupPointsForCanteen(canteenId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать новую точку питания (для роли ADMIN)")
    public CanteenDto createCanteen(@Valid @RequestBody CreateCanteenRequestDto request) {
        return canteenService.createCanteen(request);
    }

    @PostMapping("/{canteenId}/pickup-points")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавить окно экспресс-выдачи в столовую (для ролей VENDOR / ADMIN)")
    public PickupPointDto addPickupPoint(
            @PathVariable Long canteenId,
            @Valid @RequestBody CreatePickupPointRequestDto request
    ) {
        return canteenService.addPickupPoint(canteenId, request);
    }
}