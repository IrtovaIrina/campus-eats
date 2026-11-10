package ru.campuseats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.campuseats.dto.FavoriteDishDto;
import ru.campuseats.dto.FavoriteToggleResponseDto;
import ru.campuseats.entity.Dish;
import ru.campuseats.entity.FavoriteDish;
import ru.campuseats.entity.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DishMapper.class})
public interface FavoriteDishMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "dish", source = "dish")
    FavoriteDishDto toDto(FavoriteDish favoriteDish);

    List<FavoriteDishDto> toDtoList(List<FavoriteDish> favorites);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "dish", source = "dish")
    @Mapping(target = "addedAt", expression = "java(java.time.LocalDateTime.now())")
    FavoriteDish toEntity(User user, Dish dish);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "dishId", source = "dish.id")
    @Mapping(target = "dishName", source = "dish.name")
    @Mapping(target = "isFavorite", source = "isFavorite")
    @Mapping(target = "message", source = "message")
    FavoriteToggleResponseDto toToggleResponseDto(User user, Dish dish, boolean isFavorite, String message);
}