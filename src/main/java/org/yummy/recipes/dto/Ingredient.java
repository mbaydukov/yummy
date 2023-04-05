package org.yummy.recipes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Ingredient DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {

    @Schema(description = "Name of ingredient")
    @Size(max = 255, min = 1, message = "Ingredient name cannot exceed 255 characters")
    @NotBlank(message = "Ingredient name cannot be blank")
    private String name;
}
