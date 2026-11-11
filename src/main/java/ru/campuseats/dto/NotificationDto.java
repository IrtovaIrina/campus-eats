package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Уведомление пользователя")
public class NotificationDto {

    private Long id;
    private Long userId;
    private Long orderId;
    private String orderNumber;
    private String title;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
