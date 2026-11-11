package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Диетический тег блюда")
public class DietaryTagDto {

    private Long id;
    private String name;
    private String code;
    private String badgeColor;
}