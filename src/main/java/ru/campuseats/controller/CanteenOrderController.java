package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.campuseats.dto.OrderResponseDto;
import ru.campuseats.dto.UpdateOrderStatusRequestDto;
import ru.campuseats.enums.OrderStatus;
import ru.campuseats.service.CanteenOrderService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/canteen/orders")
@RequiredArgsConstructor
@Tag(name = "Заказы в столовых", description = "Очередь заказов и смена статусов приготовления")
public class CanteenOrderController {

    private final CanteenOrderService canteenOrderService;

    @GetMapping("/canteen/{canteenId}")
    @Operation(summary = "Получить очередь заказов точки питания")
    public List<OrderResponseDto> getCanteenQueue(
            @PathVariable Long canteenId,
            @RequestParam(required = false) OrderStatus status
    ) {
        return canteenOrderService.getCanteenQueue(canteenId, status);
    }

    @PatchMapping("/{orderId}/status")
    @Operation(summary = "Изменить статус приготовления заказа")
    public OrderResponseDto updateStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequestDto request
    ) {
        return canteenOrderService.updateOrderStatus(orderId, request);
    }
}