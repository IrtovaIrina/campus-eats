package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Избранное блюдо в личном кабинете студента")
public class FavoriteDishDto {

    @Schema(description = "ID записи в избранном", example = "1")
    private Long id;

    @Schema(description = "ID пользователя", example = "1")
    private Long userId;

    @Schema(description = "Карточка блюда со всеми характеристиками (цена, КБЖУ, точка питания)")
    private DishSummaryDto dish;

    @Schema(description = "Дата и время добавления в избранное")
    private LocalDateTime addedAt;
}