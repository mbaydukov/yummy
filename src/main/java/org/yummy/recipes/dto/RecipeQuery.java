package org.yummy.recipes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Recipe structured query object
 */
@Getter
@Builder
public class RecipeQuery {

    @Schema(description = "Is the recipe vegetarian.")
    private Boolean vegetarian;

    @Schema(description = "Name of ingredient")
    private Integer numberOfServings;

    @Schema(description = "List of ingredients that must be present")
    private List<Ingredient> includedIngredients;

    @Schema(description = "List of ingredients that must be absent")
    private List<Ingredient> excludedIngredients;

    @Schema(description = "Keyword that must be present within recipe cooking instruction. Special characters '*' and '?' are not acceptable and will be removed from the keyword")
    private String instruction;

    @Schema(description = "The number of result page")
    @Min(value = 0, message = "Page number should be greater than 0")
    @NotNull
    @Builder.Default
    private int pageNumber = 0;

    @Schema(description = "The size of result page")
    @Min(value = 0, message = "Page number should be greater than 0")
    @Max(value = 1000, message = "Page number should be less than or equal to 1000")
    @NotNull
    @Builder.Default
    private int pageSize = 100;

}
