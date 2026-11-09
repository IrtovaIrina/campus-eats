package ru.campuseats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.campuseats.dto.FavoriteDishDto;
import ru.campuseats.entity.FavoriteDish;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DishMapper.class})
public interface FavoriteDishMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "dish", source = "dish")
    FavoriteDishDto toDto(FavoriteDish favoriteDish);

    List<FavoriteDishDto> toDtoList(List<FavoriteDish> favorites);
}