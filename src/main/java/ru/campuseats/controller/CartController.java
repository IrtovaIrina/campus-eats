package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.campuseats.dto.AddToCartRequestDto;
import ru.campuseats.dto.CartSummaryDto;
import ru.campuseats.service.CartService;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "05. Корзина предзаказа", description = "Управление корзиной студента, авторасчёт стоимости и КБЖУ, подгрузка окон экспресс-выдачи (Шаг 3 ключевого сценария)")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Получить текущую корзину пользователя с расчётом суммы, КБЖУ и доступными окнами выдачи")
    public CartSummaryDto getUserCart(
            @Parameter(description = "ID студента", required = true, example = "1")
            @RequestParam Long userId
    ) {
        return cartService.getUserCart(userId);
    }

    @PostMapping("/items")
    @Operation(summary = "Добавить блюдо в корзину (контролирует принадлежность блюд одной точке питания)")
    public CartSummaryDto addItemToCart(@Valid @RequestBody AddToCartRequestDto request) {
        return cartService.addItemToCart(request);
    }

    @PatchMapping("/items/{cartItemId}")
    @Operation(summary = "Изменить количество порций позиции в корзине (если quantity <= 0 — позиция удаляется)")
    public CartSummaryDto updateQuantity(
            @PathVariable Long cartItemId,
            @Parameter(description = "Новое количество порций", required = true, example = "3")
            @RequestParam Integer quantity
    ) {
        return cartService.updateItemQuantity(cartItemId, quantity);
    }

    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Удалить конкретную позицию из корзины")
    public CartSummaryDto removeItem(@PathVariable Long cartItemId) {
        return cartService.removeCartItem(cartItemId);
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Полностью очистить корзину пользователя")
    public CartSummaryDto clearCart(
            @Parameter(description = "ID студента", required = true, example = "1")
            @RequestParam Long userId
    ) {
        return cartService.clearCart(userId);
    }
}