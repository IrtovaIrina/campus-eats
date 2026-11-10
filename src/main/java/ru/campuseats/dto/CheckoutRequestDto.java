package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на оформление предзаказа из корзины (Шаг 4 ключевого сценария)")
public class CheckoutRequestDto {

    @NotNull(message = "Укажите ID пользователя")
    @Schema(description = "ID студента", example = "1")
    private Long userId;

    @NotNull(message = "Выберите окно экспресс-выдачи")
    @Schema(description = "ID окна выдачи в столовой", example = "1")
    private Long pickupPointId;

    @NotBlank(message = "Выберите время получения заказа")
    @Size(max = 60, message = "Слот времени не должен превышать 60 символов")
    @Schema(description = "Желаемый слот времени получения без очереди", example = "11:50 - 12:05 (Большая перемена)")
    private String pickupTimeSlot;

    @Size(max = 300, message = "Комментарий к заказу не должен превышать 300 символов")
    @Schema(description = "Общий комментарий к заказу (необязательно)", example = "Положите, пожалуйста, приборы на 2 персоны и салфетки")
    private String comment;
}