package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.campuseats.enums.OrderStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Этап выполнения заказа для прогресс-бара")
public class TrackingStepDto {

    @Schema(description = "Код статуса этапа", example = "COOKING")
    private OrderStatus status;

    @Schema(description = "Название этапа", example = "Готовится")
    private String title;

    @Schema(description = "Подсказка по этапу", example = "Повар столовой собирает и готовит ваши блюда")
    private String description;

    @Schema(description = "Пройден ли этап", example = "true")
    private boolean completed;

    @Schema(description = "Является ли этап текущим активным", example = "true")
    private boolean current;
}