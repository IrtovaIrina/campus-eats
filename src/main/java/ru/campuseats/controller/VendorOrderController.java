package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.campuseats.dto.OrderResponseDto;
import ru.campuseats.dto.UpdateOrderStatusRequestDto;
import ru.campuseats.enums.OrderStatus;
import ru.campuseats.service.VendorOrderService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vendor/orders")
@RequiredArgsConstructor
@Tag(name = "08. Управление заказами (ЛК Точки питания)", description = "Очередь заказов повара/менеджера и смена статусов с генерацией пуш-уведомлений")
public class VendorOrderController {

    private final VendorOrderService vendorOrderService;

    @GetMapping("/canteen/{canteenId}")
    @Operation(summary = "Получить текущую очередь заказов для столовой с фильтрацией по статусу")
    public List<OrderResponseDto> getCanteenQueue(
            @PathVariable Long canteenId,
            @RequestParam(required = false) OrderStatus status
    ) {
        return vendorOrderService.getCanteenQueue(canteenId, status);
    }

    @PatchMapping("/{orderId}/status")
    @Operation(
            summary = "Изменить статус заказа по цепочке (ACCEPTED → COOKING → READY → COMPLETED)",
            description = "Переводит заказ в новый статус и автоматически генерирует персональное пуш-уведомление для студента"
    )
    public OrderResponseDto updateStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequestDto request
    ) {
        return vendorOrderService.updateOrderStatus(orderId, request);
    }
}