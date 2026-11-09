package ru.campuseats.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Категория меню")
public class CategoryDto {

    @Schema(description = "ID категории", example = "1")
    private Long id;

    @Schema(description = "Название категории", example = "Горячие блюда и боулы")
    private String name;

    @Schema(description = "Символьный код (slug)", example = "hot-dishes")
    private String slug;

    @Schema(description = "Порядок отображения", example = "2")
    private Integer displayOrder;
}