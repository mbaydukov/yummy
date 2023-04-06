package org.yummy.recipes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yummy.recipes.dto.RecipeDto;
import org.yummy.recipes.dto.RecipeQuery;
import org.yummy.recipes.mapper.RecipeMapper;
import org.yummy.recipes.service.RecipeService;

import javax.validation.Valid;
import java.util.List;

/**
 * Recipe Search controller
 * Provides a structured recipe search with paging capability
 */
@RestController
@RequestMapping("/api/recipes/search")
public class RecipeSearchController {

    private final RecipeService recipeService;
    private static final RecipeMapper mapper = RecipeMapper.INSTANCE;

    public RecipeSearchController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Operation(description = "Pageable search of recipes by structured request", responses = {
            @ApiResponse(responseCode = "200", description = "Recipe was found", content = {@Content(
                    mediaType = "application/json", schema = @Schema(implementation = RecipeDto.class)
            )}),
            @ApiResponse(responseCode = "400", description = "Bad Request - Incorrect recipe query provided")
    })
    @PostMapping
    public List<RecipeDto> search(@RequestBody @Valid RecipeQuery recipeQuery) {
        return mapper.map(recipeService.search(recipeQuery));
    }
}
