package org.yummy.recipes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Recipe structured query object
 */
@Getter
@Builder
public class RecipeQuery {

    @Schema(description = "Is the recipe vegetarian. TRUE - vegetarian, FALSE - non-vegetarian, NULL - filter is not applied")
    private Boolean vegetarian;

    @Schema(description = "Number of portions. Exact match is used. NULL - filter is not applied")
    private Integer numberOfServings;

    @Schema(description = "List of ingredients that must be present. Case insensitive. Complete phrases match is used")
    private List<Ingredient> includedIngredients;

    @Schema(description = "List of ingredients that must be absent. Case insensitive. Complete phrases match is used")
    private List<Ingredient> excludedIngredients;

    @Schema(description = "Keyword that must be present within recipe cooking instruction. " +
            "Case insensitive. Multiple keywords could be provided via space separator. " +
            "The search results will include one or more of provided keywords")
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
