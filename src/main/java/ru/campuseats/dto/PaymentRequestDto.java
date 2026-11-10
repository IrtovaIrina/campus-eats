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
@Schema(description = "Запрос на проведение демо-оплаты предзаказа")
public class PaymentRequestDto {

    @NotNull(message = "Укажите ID заказа")
    @Schema(description = "ID оформленного заказа", example = "1")
    private Long orderId;

    @NotNull(message = "Выберите способ оплаты")
    @Schema(description = "Способ оплаты (SBP, CAMPUS_CARD, BANK_CARD)", example = "SBP")
    private PaymentMethod paymentMethod;

    @Schema(description = "Списать доступные кампусные бонусы для скидки (до 50% чека)", example = "true")
    @Builder.Default
    private Boolean useBonusPoints = false;
}