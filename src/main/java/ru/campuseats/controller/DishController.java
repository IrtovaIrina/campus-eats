package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.campuseats.dto.*;
import ru.campuseats.service.DishCatalogService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dishes")
@RequiredArgsConstructor
@Tag(name = "Каталог блюд", description = "Поиск, фильтрация и управление позициями меню")
public class DishController {

    private final DishCatalogService dishCatalogService;

    @GetMapping
    @Operation(summary = "Поиск и фильтрация каталога блюд")
    public List<DishSummaryDto> searchAndFilterDishes(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long canteenId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String dietaryTagCode,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer maxCalories,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(defaultValue = "rating") String sortBy
    ) {
        return dishCatalogService.searchAndFilterDishes(
                search, canteenId, categoryId, dietaryTagCode, maxPrice, maxCalories, onlyAvailable, sortBy
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить карточку блюда по ID")
    public DishDetailDto getDishDetails(@PathVariable Long id) {
        return dishCatalogService.getDishDetails(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавить новое блюдо в меню")
    public DishDetailDto createDish(@Valid @RequestBody CreateDishRequestDto request) {
        return dishCatalogService.createDish(request);
    }

    @PatchMapping("/{id}/availability")
    @Operation(summary = "Изменить статус наличия блюда (стоп-лист)")
    public DishDetailDto updateAvailability(
            @PathVariable Long id,
            @RequestParam boolean available
    ) {
        return dishCatalogService.updateDishAvailability(id, available);
    }

    @GetMapping("/categories")
    @Operation(summary = "Получить список категорий меню")
    public List<CategoryDto> getCategories() {
        return dishCatalogService.getAllCategories();
    }

    @GetMapping("/dietary-tags")
    @Operation(summary = "Получить список диетических тегов")
    public List<DietaryTagDto> getDietaryTags() {
        return dishCatalogService.getAllDietaryTags();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактировать карточку блюда")
    public DishDetailDto updateDish(
            @PathVariable Long id,
            @Valid @RequestBody CreateDishRequestDto request
    ) {
        return dishCatalogService.updateDish(id, request, null);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить блюдо")
    public void deleteDish(@PathVariable Long id) {
        dishCatalogService.deleteDish(id);
    }
}