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
@Schema(description = "Запрос на добавление блюда в корзину")
public class AddToCartRequestDto {

    @NotNull(message = "Укажите ID пользователя")
    private Long userId;

    @NotNull(message = "Укажите ID блюда")
    private Long dishId;

    @NotNull(message = "Укажите количество порций")
    @Min(value = 1, message = "Количество порций должно быть не менее 1")
    @Max(value = 20, message = "Максимум 20 порций одного блюда в заказе")
    private Integer quantity;

    @Size(max = 250, message = "Пожелание к блюду не должно превышать 250 символов")
    private String specialRequest;

    @Builder.Default
    private Boolean clearIfDifferentCanteen = false;
}