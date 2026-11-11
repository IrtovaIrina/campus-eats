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
@Schema(description = "Запрос на изменение статуса заказа")
public class UpdateOrderStatusRequestDto {

    @NotNull(message = "Укажите новый статус заказа")
    private OrderStatus status;
}