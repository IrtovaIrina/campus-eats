package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на добавление блюда в корзину предзаказа")
public class AddToCartRequestDto {

    @NotNull(message = "Укажите ID пользователя")
    @Schema(description = "ID студента", example = "1")
    private Long userId;

    @NotNull(message = "Укажите ID блюда")
    @Schema(description = "ID выбранного блюда", example = "10")
    private Long dishId;

    @NotNull(message = "Укажите количество порций")
    @Min(value = 1, message = "Количество порций должно быть не менее 1")
    @Max(value = 20, message = "Максимум 20 порций одного блюда в предзаказе")
    @Schema(description = "Количество порций", example = "2")
    private Integer quantity;

    @Size(max = 250, message = "Пожелание к блюду не должно превышать 250 символов")
    @Schema(description = "Пожелание повару (необязательно)", example = "Соус подать отдельно, без зелёного лука")
    private String specialRequest;

    @Schema(description = "Очистить корзину автоматически, если в ней лежат блюда из другой столовой", example = "true")
    @Builder.Default
    private Boolean clearIfDifferentCanteen = false;
}