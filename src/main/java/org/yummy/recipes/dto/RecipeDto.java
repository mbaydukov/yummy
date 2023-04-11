package org.yummy.recipes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yummy.recipes.validation.Create;
import org.yummy.recipes.validation.Update;

import jakarta.validation.constraints.*;
import java.util.List;

/**
 * Recipe DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDto {

    @Schema(description = "Recipe id")
    @Null(groups = Create.class)
    @NotNull(groups = Update.class)
    private String id;

    @Schema(description = "Recipe title")
    @Size(max = 255, min = 1, message = "Recipe name cannot exceed 255 characters")
    @NotBlank(message = "Recipe name cannot be blank")
    private String name;

    @Schema(description = "Cooking instruction")
    @Size(min = 1)
    @NotBlank(message = "Recipe instruction cannot be blank")
    private String instruction;

    @Schema(description = "Whether recipe is vegetarian. Possible values (true, false, null). null - means UNKNOWN")
    @NotNull(message = "Vegetarian specification must be provided")
    private Boolean vegetarian;

    @Schema(description = "Number of portions. null - means UNKNOWN")
    @Min(value = 1, message = "Number of portions must be greater than 0")
    private Integer numberOfServings;

    @Schema(description = "List of used ingredients")
    @NotEmpty(message = "List of ingredients cannot be empty")
    private List<Ingredient> ingredients;
}
