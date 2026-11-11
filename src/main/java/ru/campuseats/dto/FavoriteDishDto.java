package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Избранное блюдо пользователя")
public class FavoriteDishDto {

    private Long id;
    private Long userId;
    private DishSummaryDto dish;
    private LocalDateTime addedAt;
}