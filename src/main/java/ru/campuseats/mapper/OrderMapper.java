package ru.campuseats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.campuseats.dto.*;
import ru.campuseats.entity.*;
import ru.campuseats.enums.OrderStatus;

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
    @Mapping(target = "statusDisplayName", expression = "java(order.getStatus() != null ? order.getStatus().getDisplayName() : \"\")")
    OrderResponseDto toResponseDto(FoodOrder order);

    List<OrderResponseDto> toResponseDtoList(List<FoodOrder> orders);

    @Mapping(target = "dishId", source = "dish.id")
    @Mapping(target = "dishImageUrl", source = "dish.imageUrl")
    @Mapping(target = "subtotalPrice", expression = "java(calculateSubtotal(item))")
    OrderItemDto toItemDto(OrderItem item);

    List<OrderItemDto> toItemDtoList(List<OrderItem> items);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNumber", source = "orderNumber")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "canteen", source = "canteen")
    @Mapping(target = "pickupTimeSlot", expression = "java(request.getPickupTimeSlot().trim())")
    @Mapping(target = "comment", expression = "java(request.getComment() != null ? request.getComment().trim() : null)")
    @Mapping(target = "totalAmount", expression = "java(java.math.BigDecimal.ZERO)")
    @Mapping(target = "totalCalories", constant = "0")
    @Mapping(target = "status", expression = "java(ru.campuseats.enums.OrderStatus.ACCEPTED)")
    @Mapping(target = "isPaid", constant = "false")
    @Mapping(target = "items", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    FoodOrder toOrderEntity(
            CheckoutRequestDto request,
            User user,
            Canteen canteen,
            String orderNumber
    );

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "dish", source = "cartItem.dish")
    @Mapping(target = "dishNameSnapshot", source = "cartItem.dish.name")
    @Mapping(target = "quantity", source = "cartItem.quantity")
    @Mapping(target = "unitPrice", source = "cartItem.dish.price")
    @Mapping(target = "caloriesPerUnit", source = "cartItem.dish.calories")
    @Mapping(target = "specialRequest", source = "cartItem.specialRequest")
    OrderItem toOrderItemEntity(CartItem cartItem);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "title", source = "status.displayName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "completed", source = "completed")
    @Mapping(target = "current", source = "current")
    TrackingStepDto toTrackingStepDto(OrderStatus status, String description, boolean completed, boolean current);

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "orderNumber", source = "order.orderNumber")
    @Mapping(target = "currentStatus", source = "order.status")
    @Mapping(target = "currentStatusDisplayName", expression = "java(order.getStatus().getDisplayName())")
    @Mapping(target = "progressPercent", source = "progressPercent")
    @Mapping(target = "statusHeadline", source = "statusHeadline")
    @Mapping(target = "canteenName", source = "order.canteen.name")
    @Mapping(target = "canteenLocation", expression = "java(order.getCanteen().getBuilding() + \" (\" + order.getCanteen().getFloor() + \" этаж)\")")
    @Mapping(target = "pickupTimeSlot", source = "order.pickupTimeSlot")
    @Mapping(target = "isPaid", source = "order.isPaid")
    @Mapping(target = "canCancel", expression = "java(order.getStatus() == ru.campuseats.enums.OrderStatus.ACCEPTED)")
    @Mapping(target = "steps", source = "steps")
    @Mapping(target = "updatedAt", source = "order.updatedAt")
    OrderTrackingDto toTrackingDto(
            FoodOrder order,
            Integer progressPercent,
            String statusHeadline,
            List<TrackingStepDto> steps
    );

    default BigDecimal calculateSubtotal(OrderItem item) {
        if (item == null || item.getUnitPrice() == null || item.getQuantity() == null) {
            return BigDecimal.ZERO;
        }
        return item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }
}