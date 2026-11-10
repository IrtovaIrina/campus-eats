package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.campuseats.dto.NotificationDto;
import ru.campuseats.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "09. Пуш-уведомления", description = "Получение списка уведомлений о смене статусов заказов и отметка о прочтении")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Получить все пуш-уведомления пользователя")
    public List<NotificationDto> getUserNotifications(@RequestParam Long userId) {
        return notificationService.getUserNotifications(userId);
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Отметить уведомление как прочитанное")
    public NotificationDto markAsRead(@PathVariable Long id) {
        return notificationService.markAsRead(id);
    }
}