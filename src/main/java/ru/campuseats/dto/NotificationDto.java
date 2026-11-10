package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Пуш-уведомление о статусе заказа")
public class NotificationDto {

    @Schema(description = "ID уведомления", example = "1")
    private Long id;

    @Schema(description = "ID пользователя", example = "1")
    private Long userId;

    @Schema(description = "ID заказа", example = "1")
    private Long orderId;

    @Schema(description = "Номер заказа", example = "CE-101")
    private String orderNumber;

    @Schema(description = "Заголовок уведомления", example = "Заказ готовится 🍳")
    private String title;

    @Schema(description = "Текст пуш-сообщения", example = "Повар столовой начал приготовление вашего предзаказа #CE-101.")
    private String message;

    @Schema(description = "Прочитано ли уведомление", example = "false")
    private Boolean isRead;

    @Schema(description = "Дата и время создания")
    private LocalDateTime createdAt;
}
