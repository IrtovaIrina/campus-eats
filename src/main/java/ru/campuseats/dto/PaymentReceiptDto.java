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
@Schema(description = "Квитанция об оплате заказа")
public class PaymentReceiptDto {

    private Long id;
    private Long orderId;
    private String orderNumber;
    private String canteenName;
    private PaymentMethod paymentMethod;
    private String paymentMethodDisplayName;
    private String transactionReference;
    private BigDecimal amountPaid;
    private String status;
    private LocalDateTime paidAt;
}