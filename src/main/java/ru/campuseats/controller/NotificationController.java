package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.campuseats.dto.NotificationDto;
import ru.campuseats.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Уведомления", description = "Уведомления пользователей о заказах")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Получить уведомления пользователя")
    public List<NotificationDto> getUserNotifications(@RequestParam Long userId) {
        return notificationService.getUserNotifications(userId);
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Отметить уведомление прочитанным")
    public NotificationDto markAsRead(@PathVariable Long id) {
        return notificationService.markAsRead(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить уведомление")
    public void deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
    }
}