package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.campuseats.enums.PaymentMethod;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на оплату заказа")
public class PaymentRequestDto {

    @NotNull(message = "Укажите ID заказа")
    private Long orderId;

    @NotNull(message = "Выберите способ оплаты")
    private PaymentMethod paymentMethod;
}