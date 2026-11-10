package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.campuseats.enums.OrderStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на перевод заказа в следующий статус")
public class UpdateOrderStatusRequestDto {

    @NotNull(message = "Укажите новый статус заказа")
    @Schema(description = "Новый статус заказа (COOKING, READY, COMPLETED, CANCELLED)", example = "COOKING")
    private OrderStatus status;
}