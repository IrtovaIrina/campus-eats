package ru.campuseats.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.campuseats.dto.CategoryDto;
import ru.campuseats.dto.DietaryTagDto;
import ru.campuseats.dto.DishSummaryDto;
import ru.campuseats.service.DishCatalogService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dishes")
@RequiredArgsConstructor
@Tag(name = "03. Каталог блюд и Фильтрация", description = "Поиск по ключевым словам и фильтрация меню по 5 параметрам (точка питания, категория, диета, цена, калории)")
public class DishController {

    private final DishCatalogService dishCatalogService;

    @GetMapping
    @Operation(
            summary = "Поиск и фильтрация каталога блюд по 5+ параметрам",
            description = "Поддерживает полнотекстовый поиск по названию и составу блюда, а также фильтрацию по точке питания, категории, диетическому тегу, максимальной цене и калорийности"
    )
    public List<DishSummaryDto> searchAndFilterDishes(
            @Parameter(description = "Ключевое слово (в названии или составе блюда)")
            @RequestParam(required = false) String search,

            @Parameter(description = "Фильтр 1: ID точки питания кампуса")
            @RequestParam(required = false) Long canteenId,

            @Parameter(description = "Фильтр 2: ID категории меню")
            @RequestParam(required = false) Long categoryId,

            @Parameter(description = "Фильтр 3: Код диетического тега (VEGAN, GLUTEN_FREE, HALAL, SUGAR_FREE, HIGH_PROTEIN)")
            @RequestParam(required = false) String dietaryTagCode,

            @Parameter(description = "Фильтр 4: Максимальная цена (руб.)")
            @RequestParam(required = false) BigDecimal maxPrice,

            @Parameter(description = "Фильтр 5: Максимальная калорийность (ккал)")
            @RequestParam(required = false) Integer maxCalories,

            @Parameter(description = "Показывать только доступные к заказу блюда")
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,

            @Parameter(description = "Сортировка: rating (по умолчанию), price_asc, price_desc, calories_asc")
            @RequestParam(defaultValue = "rating") String sortBy
    ) {
        return dishCatalogService.searchAndFilterDishes(
                search, canteenId, categoryId, dietaryTagCode, maxPrice, maxCalories, onlyAvailable, sortBy
        );
    }

    @GetMapping("/categories")
    @Operation(summary = "Получить список всех категорий меню")
    public List<CategoryDto> getCategories() {
        return dishCatalogService.getAllCategories();
    }

    @GetMapping("/dietary-tags")
    @Operation(summary = "Получить список всех диетических тегов (Вегетарианское, Без глютена и т.д.)")
    public List<DietaryTagDto> getDietaryTags() {
        return dishCatalogService.getAllDietaryTags();
    }
}