package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Аналитика и статистика", description = "Показатели продаж, выручки и нагрузки точек питания")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    @Operation(summary = "Получить сводную статистику по кампусу или конкретной точке питания")
    public CampusAnalyticsDto getDashboard(@RequestParam(required = false) Long canteenId) {
        return analyticsService.getCampusDashboard(canteenId);
    }
}