package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.campuseats.dto.CartSummaryDto;
import ru.campuseats.dto.CheckoutRequestDto;
import ru.campuseats.dto.OrderResponseDto;
import ru.campuseats.dto.OrderTrackingDto;
import ru.campuseats.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Заказы студентов", description = "Оформление предзаказа, отслеживание статуса, история и повтор заказа")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Оформить предзаказ из корзины")
    public OrderResponseDto checkoutOrder(@Valid @RequestBody CheckoutRequestDto request) {
        return orderService.checkoutFromCart(request);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Получить заказ по ID")
    public OrderResponseDto getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить историю заказов пользователя")
    public List<OrderResponseDto> getUserOrders(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "false") boolean activeOnly
    ) {
        return orderService.getUserOrderHistory(userId, activeOnly);
    }

    @GetMapping("/{orderId}/tracking")
    @Operation(summary = "Отслеживание статуса выполнения заказа")
    public OrderTrackingDto trackOrder(@PathVariable Long orderId) {
        return orderService.trackOrder(orderId);
    }

    @PostMapping("/{orderId}/repeat")
    @Operation(summary = "Повторить заказ")
    public CartSummaryDto repeatOrder(@PathVariable Long orderId) {
        return orderService.repeatOrderToCart(orderId);
    }

    @PatchMapping("/{orderId}/cancel")
    @Operation(summary = "Отменить заказ")
    public OrderResponseDto cancelOrder(
            @PathVariable Long orderId,
            @RequestParam Long userId
    ) {
        return orderService.cancelOrderByStudent(orderId, userId);
    }
}