package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.campuseats.dto.CheckoutRequestDto;
import ru.campuseats.dto.OrderResponseDto;
import ru.campuseats.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "06. Оформление предзаказа и Заказы", description = "Выбор окна выдачи, слота времени получения и создание предзаказа (Шаг 4 ключевого сценария)")
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
}
