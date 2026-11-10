package ru.campuseats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.campuseats.dto.OrderItemDto;
import ru.campuseats.dto.OrderResponseDto;
import ru.campuseats.entity.FoodOrder;
import ru.campuseats.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userFullName", source = "user.fullName")
    @Mapping(target = "userStudentGroup", source = "user.studentGroup")
    @Mapping(target = "canteenId", source = "canteen.id")
    @Mapping(target = "canteenName", source = "canteen.name")
    @Mapping(target = "canteenBuilding", source = "canteen.building")
    @Mapping(target = "pickupPointId", source = "pickupPoint.id")
    @Mapping(target = "pickupWindowCode", source = "pickupPoint.windowCode")
    @Mapping(target = "pickupLocationHint", source = "pickupPoint.locationHint")
    @Mapping(target = "statusDisplayName", expression = "java(order.getStatus() != null ? order.getStatus().getDisplayName() : \"\")")
    OrderResponseDto toResponseDto(FoodOrder order);

    List<OrderResponseDto> toResponseDtoList(List<FoodOrder> orders);

    @Mapping(target = "dishId", source = "dish.id")
    @Mapping(target = "dishImageUrl", source = "dish.imageUrl")
    @Mapping(target = "subtotalPrice", expression = "java(calculateSubtotal(item))")
    OrderItemDto toItemDto(OrderItem item);

    List<OrderItemDto> toItemDtoList(List<OrderItem> items);

    default BigDecimal calculateSubtotal(OrderItem item) {
        if (item == null || item.getUnitPrice() == null || item.getQuantity() == null) {
            return BigDecimal.ZERO;
        }
        return item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }
}
