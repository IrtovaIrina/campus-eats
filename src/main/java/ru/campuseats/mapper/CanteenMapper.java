package ru.campuseats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.campuseats.dto.CanteenDto;
import ru.campuseats.dto.CreateCanteenRequestDto;
import ru.campuseats.entity.Canteen;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CanteenMapper {

    @Mapping(target = "managerId", source = "manager.id")
    @Mapping(target = "managerName", source = "manager.fullName")
    CanteenDto toDto(Canteen canteen);

    List<CanteenDto> toDtoList(List<Canteen> canteens);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", constant = "5.0")
    @Mapping(target = "reviewsCount", constant = "0")
    @Mapping(target = "isOpen", constant = "true")
    @Mapping(target = "manager", ignore = true)
    Canteen toEntity(CreateCanteenRequestDto request);
}