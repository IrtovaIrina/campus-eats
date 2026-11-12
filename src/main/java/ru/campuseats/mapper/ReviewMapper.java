package ru.campuseats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.campuseats.dto.CreateReviewRequestDto;
import ru.campuseats.dto.ReviewDto;
import ru.campuseats.entity.Dish;
import ru.campuseats.entity.FoodOrder;
import ru.campuseats.entity.Review;
import ru.campuseats.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userFullName", source = "user.fullName")
    @Mapping(target = "userStudentGroup", source = "user.studentGroup")
    @Mapping(target = "dishId", source = "dish.id")
    @Mapping(target = "dishName", source = "dish.name")
    @Mapping(target = "canteenId", source = "dish.canteen.id")
    @Mapping(target = "canteenName", source = "dish.canteen.name")
    @Mapping(target = "orderNumber", expression = "java(review.getOrder() != null ? review.getOrder().getOrderNumber() : null)")
    ReviewDto toDto(Review review);

    List<ReviewDto> toDtoList(List<Review> reviews);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "dish", source = "dish")
    @Mapping(target = "order", source = "order")
    @Mapping(target = "rating", source = "request.rating")
    @Mapping(target = "comment", expression = "java(request.getComment().trim())")
    @Mapping(target = "canteenReply", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Review toEntity(CreateReviewRequestDto request, User user, Dish dish, FoodOrder order);
}