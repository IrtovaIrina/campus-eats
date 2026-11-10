package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.campuseats.dto.PaymentReceiptDto;
import ru.campuseats.dto.PaymentRequestDto;
import ru.campuseats.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "07. Демо-оплата и Бонусы", description = "Проведение демо-оплаты предзаказа (СБП, Кампусная карта, Банковская карта) и расчёт кэшбэка")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/pay")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Провести демо-оплату оформленного заказа",
            description = "Поддерживает оплату через СБП, кампусную или банковскую карту, списание бонусов (до 50%) и начисление 5% кэшбэка"
    )
    public PaymentReceiptDto payForOrder(@Valid @RequestBody PaymentRequestDto request) {
        return paymentService.processDemoPayment(request);
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Получить электронный чек оплаты по ID заказа")
    public PaymentReceiptDto getReceiptByOrder(@PathVariable Long orderId) {
        return paymentService.getReceiptByOrderId(orderId);
    }
}