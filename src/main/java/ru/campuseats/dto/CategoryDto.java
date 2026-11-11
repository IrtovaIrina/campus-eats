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

    private Long id;
    private String name;
    private String slug;
    private Integer displayOrder;
}