package ru.campuseats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.campuseats.dto.CreatePickupPointRequestDto;
import ru.campuseats.dto.PickupPointDto;
import ru.campuseats.entity.PickupPoint;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PickupPointMapper {

    @Mapping(target = "canteenId", source = "canteen.id")
    @Mapping(target = "canteenName", source = "canteen.name")
    PickupPointDto toDto(PickupPoint pickupPoint);

    List<PickupPointDto> toDtoList(List<PickupPoint> pickupPoints);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "canteen", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    PickupPoint toEntity(CreatePickupPointRequestDto request);
}