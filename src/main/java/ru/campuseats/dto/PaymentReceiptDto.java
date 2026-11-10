package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.campuseats.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Квитанция (электронный чек) демо-оплаты заказа")
public class PaymentReceiptDto {

    @Schema(description = "ID транзакции", example = "1")
    private Long id;

    @Schema(description = "ID заказа", example = "1")
    private Long orderId;

    @Schema(description = "Номер (пин-код) заказа", example = "CE-101")
    private String orderNumber;

    @Schema(description = "Точка питания", example = "Буфет ИРИТ-РТФ «Радиофак»")
    private String canteenName;

    @Schema(description = "Способ оплаты", example = "SBP")
    private PaymentMethod paymentMethod;

    @Schema(description = "Название способа оплаты", example = "Система быстрых платежей (QR-код)")
    private String paymentMethodDisplayName;

    @Schema(description = "Уникальный код банковской транзакции", example = "TXN-SBP-9481024")
    private String transactionReference;

    @Schema(description = "Исходная сумма заказа (руб.)", example = "680.00")
    private BigDecimal originalOrderAmount;

    @Schema(description = "Списано кампусных бонусов (руб.)", example = "100.00")
    private BigDecimal bonusUsed;

    @Schema(description = "Итого списано деньгами (руб.)", example = "580.00")
    private BigDecimal amountPaid;

    @Schema(description = "Начислено кэшбэка бонусами (5%)", example = "29.00")
    private BigDecimal cashbackEarned;

    @Schema(description = "Статус транзакции", example = "SUCCESS")
    private String status;

    @Schema(description = "Дата и время оплаты")
    private LocalDateTime paidAt;
}