package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Диетическая категория / тег блюда")
public class DietaryTagDto {

    @Schema(description = "ID тега", example = "1")
    private Long id;

    @Schema(description = "Название тега", example = "Без глютена")
    private String name;

    @Schema(description = "Код тега для фильтрации", example = "GLUTEN_FREE")
    private String code;

    @Schema(description = "Цвет бейджа в интерфейсе", example = "emerald")
    private String badgeColor;
}