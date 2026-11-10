package ru.campuseats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.campuseats.dto.AddToCartRequestDto;
import ru.campuseats.dto.CartItemDto;
import ru.campuseats.dto.CartSummaryDto;
import ru.campuseats.dto.PickupPointDto;
import ru.campuseats.entity.Canteen;
import ru.campuseats.entity.CartItem;
import ru.campuseats.entity.Dish;
import ru.campuseats.entity.User;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "dishId", source = "dish.id")
    @Mapping(target = "dishName", source = "dish.name")
    @Mapping(target = "dishImageUrl", source = "dish.imageUrl")
    @Mapping(target = "weightGrams", source = "dish.weightGrams")
    @Mapping(target = "unitPrice", source = "dish.price")
    @Mapping(target = "caloriesPerUnit", source = "dish.calories")
    @Mapping(target = "subtotalPrice", expression = "java(calculateSubtotalPrice(cartItem))")
    @Mapping(target = "subtotalCalories", expression = "java(calculateSubtotalCalories(cartItem))")
    CartItemDto toItemDto(CartItem cartItem);

    List<CartItemDto> toItemDtoList(List<CartItem> items);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "dish", source = "dish")
    @Mapping(target = "quantity", source = "request.quantity")
    @Mapping(target = "specialRequest", expression = "java(request.getSpecialRequest() != null ? request.getSpecialRequest().trim() : null)")
    @Mapping(target = "addedAt", expression = "java(java.time.LocalDateTime.now())")
    CartItem toEntity(AddToCartRequestDto request, User user, Dish dish);

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "canteenId", ignore = true)
    @Mapping(target = "canteenName", ignore = true)
    @Mapping(target = "canteenBuilding", ignore = true)
    @Mapping(target = "items", expression = "java(java.util.Collections.emptyList())")
    @Mapping(target = "totalQuantity", constant = "0")
    @Mapping(target = "totalAmount", expression = "java(java.math.BigDecimal.ZERO)")
    @Mapping(target = "totalCalories", constant = "0")
    @Mapping(target = "totalProteins", constant = "0.0")
    @Mapping(target = "totalFats", constant = "0.0")
    @Mapping(target = "totalCarbs", constant = "0.0")
    @Mapping(target = "estimatedPrepMinutes", constant = "0")
    @Mapping(target = "availablePickupPoints", expression = "java(java.util.Collections.emptyList())")
    @Mapping(target = "availableTimeSlots", source = "availableTimeSlots")
    CartSummaryDto toEmptySummaryDto(Long userId, List<String> availableTimeSlots);

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "canteenId", source = "canteen.id")
    @Mapping(target = "canteenName", source = "canteen.name")
    @Mapping(target = "canteenBuilding", source = "canteen.building")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "totalQuantity", source = "totalQuantity")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "totalCalories", source = "totalCalories")
    @Mapping(target = "totalProteins", source = "totalProteins")
    @Mapping(target = "totalFats", source = "totalFats")
    @Mapping(target = "totalCarbs", source = "totalCarbs")
    @Mapping(target = "estimatedPrepMinutes", source = "estimatedPrepMinutes")
    @Mapping(target = "availablePickupPoints", source = "availablePickupPoints")
    @Mapping(target = "availableTimeSlots", source = "availableTimeSlots")
    CartSummaryDto toSummaryDto(
            Long userId,
            Canteen canteen,
            List<CartItemDto> items,
            Integer totalQuantity,
            BigDecimal totalAmount,
            Integer totalCalories,
            Double totalProteins,
            Double totalFats,
            Double totalCarbs,
            Integer estimatedPrepMinutes,
            List<PickupPointDto> availablePickupPoints,
            List<String> availableTimeSlots
    );

    default BigDecimal calculateSubtotalPrice(CartItem item) {
        if (item == null || item.getDish() == null || item.getDish().getPrice() == null || item.getQuantity() == null) {
            return BigDecimal.ZERO;
        }
        return item.getDish().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }

    default Integer calculateSubtotalCalories(CartItem item) {
        if (item == null || item.getDish() == null || item.getDish().getCalories() == null || item.getQuantity() == null) {
            return 0;
        }
        return item.getDish().getCalories() * item.getQuantity();
    }
}