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
@Schema(description = "Запрос на оформление заказа из корзины")
public class CheckoutRequestDto {

    @NotNull(message = "Укажите ID пользователя")
    private Long userId;

    @NotBlank(message = "Выберите время получения заказа")
    @Size(max = 60, message = "Время получения не должно превышать 60 символов")
    private String pickupTimeSlot;

    @Size(max = 300, message = "Комментарий к заказу не должен превышать 300 символов")
    private String comment;
}