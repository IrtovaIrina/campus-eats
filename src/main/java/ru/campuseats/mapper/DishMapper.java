package ru.campuseats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.campuseats.dto.*;
import ru.campuseats.entity.Category;
import ru.campuseats.entity.DietaryTag;
import ru.campuseats.entity.Dish;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DishMapper {

    @Mapping(target = "canteenId", source = "canteen.id")
    @Mapping(target = "canteenName", source = "canteen.name")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    DishSummaryDto toSummaryDto(Dish dish);

    List<DishSummaryDto> toSummaryDtoList(List<Dish> dishes);

    @Mapping(target = "canteenId", source = "canteen.id")
    @Mapping(target = "canteenName", source = "canteen.name")
    @Mapping(target = "canteenBuilding", source = "canteen.building")
    @Mapping(target = "canteenFloor", source = "canteen.floor")
    @Mapping(target = "canteenOpeningHours", source = "canteen.openingHours")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "caloriesPer100g", expression = "java(calculateCaloriesPer100g(dish))")
    DishDetailDto toDetailDto(Dish dish);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "canteen", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "dietaryTags", ignore = true)
    @Mapping(target = "isAvailable", constant = "true")
    @Mapping(target = "rating", constant = "5.0")
    @Mapping(target = "prepTimeMinutes", expression = "java(request.getPrepTimeMinutes() != null ? request.getPrepTimeMinutes() : 10)")
    Dish toEntity(CreateDishRequestDto request);

    CategoryDto toCategoryDto(Category category);

    List<CategoryDto> toCategoryDtoList(List<Category> categories);

    DietaryTagDto toDietaryTagDto(DietaryTag tag);

    List<DietaryTagDto> toDietaryTagDtoList(List<DietaryTag> tags);

    default Integer calculateCaloriesPer100g(Dish dish) {
        if (dish == null || dish.getWeightGrams() == null || dish.getWeightGrams() <= 0 || dish.getCalories() == null) {
            return 0;
        }
        return Math.round((dish.getCalories() * 100.0f) / dish.getWeightGrams());
    }
}