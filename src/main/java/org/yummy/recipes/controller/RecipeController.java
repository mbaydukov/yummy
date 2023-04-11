package org.yummy.recipes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yummy.recipes.dto.RecipeDto;
import org.yummy.recipes.exeption.NotFoundException;
import org.yummy.recipes.mapper.RecipeMapper;
import org.yummy.recipes.service.RecipeService;
import org.yummy.recipes.validation.Create;
import org.yummy.recipes.validation.Update;

import jakarta.validation.Valid;

/**
 * Recipe CRUD controller
 */
@RestController
@RequestMapping("/api/recipes")
@Validated
public class RecipeController {

    private final RecipeService recipeService;
    private static final RecipeMapper mapper = RecipeMapper.INSTANCE;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @Operation(description = "Get a recipe by id", responses = {
            @ApiResponse(responseCode = "200", description = "Recipe was found", content = { @Content(
                    mediaType = "application/json", schema = @Schema(implementation = RecipeDto.class)
            )}),
            @ApiResponse(responseCode = "404", description = "Not found - The recipe was not found")
    })
    @GetMapping("{recipeId}")
    public RecipeDto getRecipeById(@Parameter(name = "id", description = "Recipe id", example = "1") @PathVariable String recipeId) {
        return mapper.map(recipeService.getRecipeById(recipeId).orElseThrow(NotFoundException::new));
    }

    @Operation(description = "Add recipe", responses = {
            @ApiResponse(responseCode = "200", description = "Recipe was successfully saved", content = { @Content(
                    mediaType = "application/json", schema = @Schema(implementation = RecipeDto.class)
            )}),
            @ApiResponse(responseCode = "400", description = "Bad Request - Incorrect recipe specification provided")
    })
    @PostMapping
    @Validated(Create.class)
    public RecipeDto addRecipe(@RequestBody @Valid RecipeDto recipeDto) {
        return mapper.map(recipeService.addRecipe(recipeDto));
    }

    @Operation(description = "Update recipe", responses = {
            @ApiResponse(responseCode = "200", description = "Recipe was successfully updated", content = { @Content(
                    mediaType = "application/json", schema = @Schema(implementation = RecipeDto.class)
            )}),
            @ApiResponse(responseCode = "400", description = "Bad Request - Incorrect recipe specification provided")
    })
    @PatchMapping
    @Validated(Update.class)
    public RecipeDto updateRecipe(@RequestBody @Valid RecipeDto recipeDto) {
        return mapper.map(recipeService.updateRecipe(recipeDto));
    }

    @Operation(description = "Delete recipe", responses = {
            @ApiResponse(responseCode = "200", description = "Recipe was successfully deleted")
    })
    @DeleteMapping("{recipeId}")
    public void delete(@Parameter(name = "id", description = "Recipe id", example = "1") @PathVariable String recipeId) {
        recipeService.deleteRecipe(recipeId);
    }
}
