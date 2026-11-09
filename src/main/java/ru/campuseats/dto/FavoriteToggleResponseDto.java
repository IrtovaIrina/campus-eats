package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Результат переключения статуса избранного блюда")
public class FavoriteToggleResponseDto {

    @Schema(description = "ID пользователя", example = "1")
    private Long userId;

    @Schema(description = "ID блюда", example = "10")
    private Long dishId;

    @Schema(description = "Название блюда", example = "Поке-боул с лососем, киноа и авокадо")
    private String dishName;

    @Schema(description = "Находится ли блюдо в избранном после операции (true — добавлено, false — удалено)", example = "true")
    private boolean isFavorite;

    @Schema(description = "Информационное сообщение для интерфейса", example = "Блюдо добавлено в избранное")
    private String message;
}