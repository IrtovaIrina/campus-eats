package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на создание окна экспресс-выдачи в столовой")
public class CreatePickupPointRequestDto {

    @NotBlank(message = "Укажите код окна выдачи")
    @Schema(description = "Код или номер окна", example = "Окно экспресс-выдачи №2")
    private String windowCode;

    @NotBlank(message = "Укажите ориентир расположения окна")
    @Schema(description = "Как найти окно без очереди", example = "Справа у кофейного островка")
    private String locationHint;

    @NotNull(message = "Укажите пропускную способность слота")
    @Min(value = 1, message = "Лимит заказов должен быть не менее 1")
    @Schema(description = "Максимум заказов за 15 минут", example = "15")
    private Integer maxOrdersPerSlot;
}