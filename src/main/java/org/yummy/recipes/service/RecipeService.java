package org.yummy.recipes.service;

import org.springframework.stereotype.Service;
import org.yummy.recipes.dto.RecipeDto;
import org.yummy.recipes.dto.RecipeQuery;
import org.yummy.recipes.entity.Recipe;
import org.yummy.recipes.mapper.RecipeMapper;
import org.yummy.recipes.repository.RecipeRepository;
import org.yummy.recipes.repository.RecipeSearchRepository;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Implements convenient methods to access CRUD and Recipes Search functionalities
 **/
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeSearchRepository recipeSearchRepository;

    public RecipeService(RecipeRepository recipeRepository, RecipeSearchRepository recipeSearchRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeSearchRepository = recipeSearchRepository;
    }

    /**
     * Return recipe by provided id
     *
     * @param recipeId Recipe id
     * @return Optional<Recipe> if required recipe was found. Optional.empty() otherwise
     */
    public Optional<Recipe> getRecipeById(@NotNull String recipeId) {
        return recipeRepository.findById(recipeId);
    }

    /**
     * Save recipe
     *
     * @param recipeDto Recipe DTO
     * @return persisted Recipe
     */
    public Recipe addRecipe(@NotNull RecipeDto recipeDto) {
        return recipeRepository.save(RecipeMapper.INSTANCE.map(recipeDto));
    }

    /**
     * Update recipe
     *
     * @param recipeDto existed Recipe DTO
     * @return persisted Recipe
     */
    public Recipe updateRecipe(@NotNull RecipeDto recipeDto) {
        return addRecipe(recipeDto);
    }

    /**
     * Delete recipe
     *
     * @param recipeId Recipe id to delete
     */
    public void deleteRecipe(@NotNull String recipeId) {
        recipeRepository.deleteById(recipeId);
    }

    /**
     * Search recipes by structured query request
     *
     * @param recipeQuery structured query request
     * @return collection of recipes that match the request
     */
    public List<Recipe> search(@NotNull RecipeQuery recipeQuery) {
        return recipeSearchRepository.search(recipeQuery);
    }
}
