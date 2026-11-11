package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.campuseats.dto.CanteenDto;
import ru.campuseats.dto.CreateCanteenRequestDto;
import ru.campuseats.service.CanteenService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/canteens")
@RequiredArgsConstructor
@Tag(name = "Точки питания", description = "Управление столовыми и кафе кампуса")
public class CanteenController {

    private final CanteenService canteenService;

    @GetMapping
    @Operation(summary = "Получить список всех точек питания")
    public List<CanteenDto> getAllCanteens(
            @RequestParam(defaultValue = "false") boolean onlyOpen
    ) {
        return canteenService.getAllCanteens(onlyOpen);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить точку питания по ID")
    public CanteenDto getCanteenById(@PathVariable Long id) {
        return canteenService.getCanteenById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать точку питания")
    public CanteenDto createCanteen(@Valid @RequestBody CreateCanteenRequestDto request) {
        return canteenService.createCanteen(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные точки питания")
    public CanteenDto updateCanteen(
            @PathVariable Long id,
            @Valid @RequestBody CreateCanteenRequestDto request,
            @RequestParam(required = false) Boolean isOpen
    ) {
        return canteenService.updateCanteen(id, request, isOpen);
    }

    @PatchMapping("/{id}/verify")
    @Operation(summary = "Подтвердить или отозвать статус точки питания")
    public CanteenDto verifyCanteen(
            @PathVariable Long id,
            @RequestParam boolean verified
    ) {
        return canteenService.verifyCanteen(id, verified);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить точку питания")
    public void deleteCanteen(@PathVariable Long id) {
        canteenService.deleteCanteen(id);
    }
}