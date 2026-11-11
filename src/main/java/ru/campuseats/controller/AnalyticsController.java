package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.campuseats.dto.CampusAnalyticsDto;
import ru.campuseats.service.AnalyticsService;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@Tag(name = "11. Аналитика загруженности и Статистика продаж (ADMIN / VENDOR)", description = "Дашборд выручки, загруженности окон экспресс-выдачи по слотам перемен и топ популярных блюд")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    @Operation(
            summary = "Получить сводную аналитику загруженности столовых, выручки и топ-5 популярных блюд",
            description = "Если передан canteenId — возвращает статистику для конкретной точки питания (ЛК Менеджера столовой). Без параметра — сводную аналитику по всему кампусу (ЛК Администратора)."
    )
    public CampusAnalyticsDto getDashboard(
            @Parameter(description = "Фильтр по ID точки питания (необязательно)")
            @RequestParam(required = false) Long canteenId
    ) {
        return analyticsService.getCampusDashboard(canteenId);
    }
}