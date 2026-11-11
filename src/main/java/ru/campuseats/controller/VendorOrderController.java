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
@Tag(name = "Заказы точки питания", description = "Очередь заказов и смена статусов приготовления")
public class VendorOrderController {

    private final VendorOrderService vendorOrderService;

    @GetMapping("/canteen/{canteenId}")
    @Operation(summary = "Получить очередь заказов точки питания")
    public List<OrderResponseDto> getCanteenQueue(
            @PathVariable Long canteenId,
            @RequestParam(required = false) OrderStatus status
    ) {
        return vendorOrderService.getCanteenQueue(canteenId, status);
    }

    @PatchMapping("/{orderId}/status")
    @Operation(summary = "Изменить статус приготовления заказа")
    public OrderResponseDto updateStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequestDto request
    ) {
        return vendorOrderService.updateOrderStatus(orderId, request);
    }
}