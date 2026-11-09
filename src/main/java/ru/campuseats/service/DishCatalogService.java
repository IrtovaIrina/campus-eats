package ru.campuseats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.campuseats.dto.CategoryDto;
import ru.campuseats.dto.DietaryTagDto;
import ru.campuseats.dto.DishSummaryDto;
import ru.campuseats.entity.Dish;
import ru.campuseats.mapper.DishMapper;
import ru.campuseats.repository.CategoryRepository;
import ru.campuseats.repository.DietaryTagRepository;
import ru.campuseats.repository.DishRepository;
import ru.campuseats.repository.DishSpecification;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DishCatalogService {

    private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository;
    private final DietaryTagRepository dietaryTagRepository;
    private final DishMapper dishMapper;

    @Transactional(readOnly = true)
    public List<DishSummaryDto> searchAndFilterDishes(
            String search,
            Long canteenId,
            Long categoryId,
            String dietaryTagCode,
            BigDecimal maxPrice,
            Integer maxCalories,
            Boolean onlyAvailable,
            String sortBy
    ) {
        Specification<Dish> spec = DishSpecification.filterDishes(
                search, canteenId, categoryId, dietaryTagCode, maxPrice, maxCalories, onlyAvailable
        );

        Sort sort = resolveSort(sortBy);
        List<Dish> dishes = dishRepository.findAll(spec, sort);
        return dishMapper.toSummaryDtoList(dishes);
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return dishMapper.toCategoryDtoList(categoryRepository.findAllByOrderByDisplayOrderAsc());
    }

    @Transactional(readOnly = true)
    public List<DietaryTagDto> getAllDietaryTags() {
        return dishMapper.toDietaryTagDtoList(dietaryTagRepository.findAllByOrderByIdAsc());
    }

    private Sort resolveSort(String sortBy) {
        if ("price_asc".equalsIgnoreCase(sortBy)) {
            return Sort.by(Sort.Direction.ASC, "price");
        }
        if ("price_desc".equalsIgnoreCase(sortBy)) {
            return Sort.by(Sort.Direction.DESC, "price");
        }
        if ("calories_asc".equalsIgnoreCase(sortBy)) {
            return Sort.by(Sort.Direction.ASC, "calories");
        }
        return Sort.by(Sort.Direction.DESC, "rating").and(Sort.by(Sort.Direction.ASC, "name"));
    }
}