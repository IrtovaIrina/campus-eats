package ru.campuseats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.campuseats.dto.*;
import ru.campuseats.entity.Canteen;
import ru.campuseats.entity.Category;
import ru.campuseats.entity.DietaryTag;
import ru.campuseats.entity.Dish;
import ru.campuseats.mapper.DishMapper;
import ru.campuseats.repository.CanteenRepository;
import ru.campuseats.repository.CategoryRepository;
import ru.campuseats.repository.DietaryTagRepository;
import ru.campuseats.repository.DishRepository;
import ru.campuseats.repository.DishSpecification;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DishCatalogService {

    private final DishRepository dishRepository;
    private final CanteenRepository canteenRepository;
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
    public DishDetailDto getDishDetails(Long dishId) {
        Dish dish = findDishOrThrow(dishId);
        return dishMapper.toDetailDto(dish);
    }

    @Transactional
    public DishDetailDto createDish(CreateDishRequestDto request) {
        Canteen canteen = canteenRepository.findById(request.getCanteenId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Точка питания с ID " + request.getCanteenId() + " не найдена"
                ));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Категория с ID " + request.getCategoryId() + " не найдена"
                ));

        Dish dish = dishMapper.toEntity(request);
        dish.setCanteen(canteen);
        dish.setCategory(category);

        if (request.getDietaryTagCodes() != null && !request.getDietaryTagCodes().isEmpty()) {
            Set<DietaryTag> tags = new HashSet<>();
            for (String code : request.getDietaryTagCodes()) {
                dietaryTagRepository.findByCodeIgnoreCase(code.trim())
                        .ifPresent(tags::add);
            }
            dish.setDietaryTags(tags);
        }

        Dish saved = dishRepository.save(dish);
        return dishMapper.toDetailDto(saved);
    }

    @Transactional
    public DishDetailDto updateDishAvailability(Long dishId, boolean available) {
        Dish dish = findDishOrThrow(dishId);
        dish.setIsAvailable(available);
        return dishMapper.toDetailDto(dishRepository.save(dish));
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return dishMapper.toCategoryDtoList(categoryRepository.findAllByOrderByDisplayOrderAsc());
    }

    @Transactional(readOnly = true)
    public List<DietaryTagDto> getAllDietaryTags() {
        return dishMapper.toDietaryTagDtoList(dietaryTagRepository.findAllByOrderByIdAsc());
    }

    public Dish findDishOrThrow(Long dishId) {
        return dishRepository.findById(dishId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Блюдо с ID " + dishId + " не найдено"
                ));
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