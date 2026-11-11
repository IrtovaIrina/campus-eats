package ru.campuseats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.campuseats.dto.*;
import ru.campuseats.entity.*;
import ru.campuseats.mapper.DishMapper;
import ru.campuseats.repository.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishCatalogService {

    private final DishRepository dishRepository;
    private final CanteenRepository canteenRepository;
    private final CategoryRepository categoryRepository;
    private final DietaryTagRepository dietaryTagRepository;
    private final CartItemRepository cartItemRepository;
    private final FavoriteDishRepository favoriteDishRepository;
    private final ReviewRepository reviewRepository;
    private final OrderItemRepository orderItemRepository;
    private final DishMapper dishMapper;

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return dishMapper.toCategoryDtoList(
                categoryRepository.findAllByOrderByDisplayOrderAsc()
        );
    }

    @Transactional(readOnly = true)
    public List<DietaryTagDto> getAllDietaryTags() {
        return dishMapper.toDietaryTagDtoList(
                dietaryTagRepository.findAllByOrderByIdAsc()
        );
    }

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
        org.springframework.data.domain.Sort sort = switch (sortBy != null ? sortBy : "rating") {
            case "price_asc" -> org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "price");
            case "price_desc" -> org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "price");
            case "calories_asc" -> org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "calories");
            default -> org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "rating");
        };

        List<Dish> filtered = dishRepository.findAll(
                DishSpecification.filterDishes(
                        search, canteenId, categoryId, dietaryTagCode, maxPrice, maxCalories, onlyAvailable
                ),
                sort
        );

        return dishMapper.toSummaryDtoList(filtered);
    }

    @Transactional
    public DishDetailDto updateDishAvailability(Long dishId, boolean available) {
        Dish dish = findDishOrThrow(dishId);
        dish.setIsAvailable(available);
        return dishMapper.toDetailDto(dishRepository.save(dish));
    }

    @Transactional(readOnly = true)
    public DishDetailDto getDishDetails(Long dishId) {
        Dish dish = findDishOrThrow(dishId);
        return dishMapper.toDetailDto(dish);
    }

    // CREATE (с загрузкой файла изображения)
    @Transactional
    public DishDetailDto createDish(CreateDishRequestDto request, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            request.setImageUrl(storeDishImage(imageFile));
        } else if (request.getImageUrl() == null || request.getImageUrl().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Загрузите изображение блюда (PNG, JPG, WEBP)"
            );
        }

        if (request.getPrepTimeMinutes() == null) {
            request.setPrepTimeMinutes(7);
        }

        return createDish(request);
    }

    // CREATE (базовый для REST API)
    @Transactional
    public DishDetailDto createDish(CreateDishRequestDto request) {
        Canteen canteen = canteenRepository.findById(request.getCanteenId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Столовая с ID " + request.getCanteenId() + " не найдена"
                ));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Категория с ID " + request.getCategoryId() + " не найдена"
                ));

        Set<DietaryTag> tags = resolveDietaryTags(request.getDietaryTagCodes());
        Dish dish = dishMapper.toEntity(request, canteen, category, tags);

        Dish saved = dishRepository.save(dish);
        return dishMapper.toDetailDto(saved);
    }

    // UPDATE (Полное редактирование карточки блюда + опциональная замена фото)
    @Transactional
    public DishDetailDto updateDish(Long dishId, CreateDishRequestDto request, MultipartFile newImageFile) {
        Dish dish = findDishOrThrow(dishId);

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Категория с ID " + request.getCategoryId() + " не найдена"
                    ));
            dish.setCategory(category);
        }

        dish.setName(request.getName().trim());
        dish.setComposition(request.getComposition().trim());
        dish.setWeightGrams(request.getWeightGrams());
        dish.setCalories(request.getCalories());
        dish.setProteins(request.getProteins() != null ? request.getProteins() : dish.getProteins());
        dish.setFats(request.getFats() != null ? request.getFats() : dish.getFats());
        dish.setCarbs(request.getCarbs() != null ? request.getCarbs() : dish.getCarbs());
        dish.setPrice(request.getPrice());

        if (request.getPrepTimeMinutes() != null) {
            dish.setPrepTimeMinutes(request.getPrepTimeMinutes());
        }

        // Если загружен новый файл картинки — обновляем фото, иначе оставляем текущее
        if (newImageFile != null && !newImageFile.isEmpty()) {
            dish.setImageUrl(storeDishImage(newImageFile));
        } else if (request.getImageUrl() != null && !request.getImageUrl().isBlank()) {
            dish.setImageUrl(request.getImageUrl().trim());
        }

        if (request.getDietaryTagCodes() != null) {
            dish.setDietaryTags(resolveDietaryTags(request.getDietaryTagCodes()));
        }

        Dish updated = dishRepository.save(dish);
        return dishMapper.toDetailDto(updated);
    }

    @Transactional
    public void deleteDish(Long dishId) {
        Dish dish = findDishOrThrow(dishId);
        cartItemRepository.deleteByDishId(dishId);
        favoriteDishRepository.deleteByDishId(dishId);
        reviewRepository.deleteByDishId(dishId);
        List<OrderItem> historyItems = orderItemRepository.findByDishId(dishId);
        for (OrderItem item : historyItems) {
            item.setDish(null);
        }
        orderItemRepository.saveAll(historyItems);
        dish.getDietaryTags().clear();
        dishRepository.delete(dish);
    }

    private Set<DietaryTag> resolveDietaryTags(List<String> codes) {
        Set<DietaryTag> tags = new LinkedHashSet<>();
        if (codes != null) {
            for (String code : codes) {
                if (code == null || code.isBlank()) continue;
                dietaryTagRepository.findByCodeIgnoreCase(code.trim())
                        .ifPresent(tags::add);
            }
        }
        return tags;
    }

    private Dish findDishOrThrow(Long dishId) {
        return dishRepository.findById(dishId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Блюдо с ID " + dishId + " не найдено"
                ));
    }

    private String storeDishImage(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = ".png";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        }

        if (!List.of(".png", ".jpg", ".jpeg", ".webp", ".gif").contains(extension)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Недопустимый формат файла. Разрешены: PNG, JPG, JPEG, WEBP"
            );
        }

        try {
            byte[] bytes = file.getBytes();

            Path uploadDir = Paths.get("uploads", "dishes").toAbsolutePath().normalize();
            Files.createDirectories(uploadDir);
            String uniqueFileName = "dish_" + UUID.randomUUID() + extension;
            Files.write(uploadDir.resolve(uniqueFileName), bytes);

            String mimeType = switch (extension) {
                case ".jpg", ".jpeg" -> "image/jpeg";
                case ".webp" -> "image/webp";
                case ".gif" -> "image/gif";
                default -> "image/png";
            };

            return "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при сохранении изображения блюда"
            );
        }
    }
}