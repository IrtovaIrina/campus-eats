package ru.campuseats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.campuseats.entity.User;
import ru.campuseats.dto.RegisterUserRequestDto;
import ru.campuseats.dto.UserDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    List<UserDto> toDtoList(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", source = "password")
    @Mapping(target = "bonusBalance", expression = "java(new java.math.BigDecimal(\"150.00\"))")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    User toEntity(RegisterUserRequestDto request);
}
