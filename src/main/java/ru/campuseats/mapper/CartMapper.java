package ru.campuseats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.campuseats.dto.CartItemDto;
import ru.campuseats.entity.CartItem;

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