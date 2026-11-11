package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "06. Оформление предзаказа, Трекинг и Повтор заказа", description = "Создание предзаказа, отслеживание статуса в реальном времени, история и повтор заказа в 1 клик (Шаги 4–5 ключевого сценария)")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Оформить предзаказ из текущей корзины (Шаг 4 ключевого сценария)",
            description = "Фиксирует состав заказа, выбранное окно экспресс-выдачи и слот времени, присваивает заказу пин-код получения и статус ACCEPTED («Принят»)"
    )
    public OrderResponseDto checkoutOrder(@Valid @RequestBody CheckoutRequestDto request) {
        return orderService.checkoutFromCart(request);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Получить детальную информацию о заказе по его ID")
    public OrderResponseDto getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить историю заказов студента (все или только активные)")
    public List<OrderResponseDto> getUserOrders(
            @PathVariable Long userId,
            @Parameter(description = "true — только активные заказы (ACCEPTED, COOKING, READY), false — вся история")
            @RequestParam(defaultValue = "false") boolean activeOnly
    ) {
        return orderService.getUserOrderHistory(userId, activeOnly);
    }

    @GetMapping("/{orderId}/tracking")
    @Operation(
            summary = "Получить экран трекинга статуса заказа с прогресс-баром (Шаг 5 ключевого сценария)",
            description = "Возвращает процент готовности, пошаговый чек-лист статусов (Принят → Готовится → Готов к выдаче → Выдан) и ориентир окна экспресс-выдачи"
    )
    public OrderTrackingDto trackOrder(@PathVariable Long orderId) {
        return orderService.trackOrder(orderId);
    }

    @PostMapping("/{orderId}/repeat")
    @Operation(
            summary = "Повторить прошлый заказ в 1 клик (ЛК Студента)",
            description = "Копирует все доступные блюда из выбранного прошлого заказа обратно в корзину студента со всеми пожеланиями повару"
    )
    public CartSummaryDto repeatOrder(@PathVariable Long orderId) {
        return orderService.repeatOrderToCart(orderId);
    }

    @PatchMapping("/{orderId}/cancel")
    @Operation(summary = "Отменить заказ студентом (доступно только пока заказ в статусе ACCEPTED)")
    public OrderResponseDto cancelOrder(
            @PathVariable Long orderId,
            @RequestParam Long userId
    ) {
        return orderService.cancelOrderByStudent(orderId, userId);
    }
}