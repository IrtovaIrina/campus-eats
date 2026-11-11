package ru.campuseats.repository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import ru.campuseats.entity.DietaryTag;
import ru.campuseats.entity.Dish;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Stream;

public final class DishSpecification {

    private DishSpecification() {
    }

    public static Specification<Dish> filterDishes(
            String search,
            Long canteenId,
            Long categoryId,
            String dietaryTagCode,
            BigDecimal maxPrice,
            Integer maxCalories,
            Boolean onlyAvailable
    ) {
        return Stream.of(
                        bySearch(search),
                        byCanteen(canteenId),
                        byCategory(categoryId),
                        byDietaryTag(dietaryTagCode),
                        byMaxPrice(maxPrice),
                        byMaxCalories(maxCalories),
                        byOnlyAvailable(onlyAvailable)
                )
                .filter(Objects::nonNull)
                .reduce(Specification.where(null), Specification::and);
    }

    private static Specification<Dish> bySearch(String search) {
        if (search == null || search.isBlank()) return null;
        String pattern = "%" + search.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("name")), pattern),
                cb.like(cb.lower(root.get("composition")), pattern)
        );
    }

    private static Specification<Dish> byCanteen(Long canteenId) {
        return canteenId == null ? null :
                (root, query, cb) -> cb.equal(root.get("canteen").get("id"), canteenId);
    }

    private static Specification<Dish> byCategory(Long categoryId) {
        return categoryId == null ? null :
                (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
    }

    private static Specification<Dish> byDietaryTag(String tagCode) {
        if (tagCode == null || tagCode.isBlank()) return null;
        return (root, query, cb) -> {
            query.distinct(true);
            Join<Dish, DietaryTag> tagJoin = root.join("dietaryTags", JoinType.INNER);
            return cb.equal(cb.upper(tagJoin.get("code")), tagCode.trim().toUpperCase());
        };
    }

    private static Specification<Dish> byMaxPrice(BigDecimal maxPrice) {
        return maxPrice == null ? null :
                (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    private static Specification<Dish> byMaxCalories(Integer maxCalories) {
        return maxCalories == null ? null :
                (root, query, cb) -> cb.lessThanOrEqualTo(root.get("calories"), maxCalories);
    }

    private static Specification<Dish> byOnlyAvailable(Boolean onlyAvailable) {
        return !Boolean.TRUE.equals(onlyAvailable) ? null :
                (root, query, cb) -> cb.and(
                        cb.isTrue(root.get("isAvailable")),
                        cb.isTrue(root.get("canteen").get("isVerified")),
                        cb.isTrue(root.get("canteen").get("isOpen"))
                );
    }
}