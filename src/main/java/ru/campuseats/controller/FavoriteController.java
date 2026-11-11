package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.campuseats.dto.FavoriteDishDto;
import ru.campuseats.dto.FavoriteToggleResponseDto;
import ru.campuseats.service.FavoriteService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
@Tag(name = "Избранное", description = "Список избранных блюд пользователя")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    @Operation(summary = "Получить избранные блюда пользователя")
    public List<FavoriteDishDto> getUserFavorites(@RequestParam Long userId) {
        return favoriteService.getUserFavorites(userId);
    }

    @PostMapping("/toggle")
    @Operation(summary = "Добавить или удалить блюдо из избранного")
    public FavoriteToggleResponseDto toggleFavorite(
            @RequestParam Long userId,
            @RequestParam Long dishId
    ) {
        return favoriteService.toggleFavorite(userId, dishId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить блюдо из избранного")
    public void removeFavorite(
            @RequestParam Long userId,
            @RequestParam Long dishId
    ) {
        favoriteService.removeFavorite(userId, dishId);
    }
}