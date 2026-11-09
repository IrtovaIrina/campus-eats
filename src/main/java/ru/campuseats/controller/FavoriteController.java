package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "04. Избранные блюда (ЛК Студента)", description = "Добавление любимых блюд в избранное для быстрого заказа из личного кабинета")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    @Operation(summary = "Получить список избранных блюд пользователя (для вкладки «Избранное» в ЛК)")
    public List<FavoriteDishDto> getUserFavorites(
            @Parameter(description = "ID студента", required = true, example = "1")
            @RequestParam Long userId
    ) {
        return favoriteService.getUserFavorites(userId);
    }

    @PostMapping("/toggle")
    @Operation(summary = "Добавить блюдо в избранное или убрать из избранного одним кликом")
    public FavoriteToggleResponseDto toggleFavorite(
            @Parameter(description = "ID студента", required = true, example = "1")
            @RequestParam Long userId,

            @Parameter(description = "ID блюда", required = true, example = "10")
            @RequestParam Long dishId
    ) {
        return favoriteService.toggleFavorite(userId, dishId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить блюдо из избранного")
    public void removeFavorite(
            @Parameter(description = "ID студента", required = true, example = "1")
            @RequestParam Long userId,

            @Parameter(description = "ID блюда", required = true, example = "10")
            @RequestParam Long dishId
    ) {
        favoriteService.removeFavorite(userId, dishId);
    }
}