package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Корзина", description = "Управление корзиной предзаказа")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Получить корзину пользователя")
    public CartSummaryDto getUserCart(@RequestParam Long userId) {
        return cartService.getUserCart(userId);
    }

    @PostMapping("/items")
    @Operation(summary = "Добавить блюдо в корзину")
    public CartSummaryDto addItemToCart(@Valid @RequestBody AddToCartRequestDto request) {
        return cartService.addItemToCart(request);
    }

    @PatchMapping("/items/{cartItemId}")
    @Operation(summary = "Изменить количество порций в корзине")
    public CartSummaryDto updateQuantity(
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity
    ) {
        return cartService.updateItemQuantity(cartItemId, quantity);
    }

    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Удалить позицию из корзины")
    public CartSummaryDto removeItem(@PathVariable Long cartItemId) {
        return cartService.removeCartItem(cartItemId);
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Очистить корзину")
    public CartSummaryDto clearCart(@RequestParam Long userId) {
        return cartService.clearCart(userId);
    }
}