package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Результат добавления или удаления блюда из избранного")
public class FavoriteToggleResponseDto {

    private Long userId;
    private Long dishId;
    private String dishName;
    private boolean isFavorite;
    private String message;
}