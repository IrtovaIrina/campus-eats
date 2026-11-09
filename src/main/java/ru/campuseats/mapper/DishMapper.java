package ru.campuseats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.campuseats.dto.CategoryDto;
import ru.campuseats.dto.DietaryTagDto;
import ru.campuseats.dto.DishSummaryDto;
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

    CategoryDto toCategoryDto(Category category);

    List<CategoryDto> toCategoryDtoList(List<Category> categories);

    DietaryTagDto toDietaryTagDto(DietaryTag tag);

    List<DietaryTagDto> toDietaryTagDtoList(List<DietaryTag> tags);
}
