package org.yummy.recipes.service;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.yummy.recipes.dto.Ingredient;
import org.yummy.recipes.dto.RecipeDto;
import org.yummy.recipes.dto.RecipeQuery;
import org.yummy.recipes.entity.Recipe;
import org.yummy.recipes.repository.RecipeRepository;
import org.yummy.recipes.repository.RecipeSearchRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class RecipeServiceTest {

    @MockBean
    private RecipeRepository recipeRepository;

    @MockBean
    private RecipeSearchRepository recipeSearchRepository;

    @Autowired
    private RecipeService recipeService;

    @Test
    void getRecipeById() {
        Mockito.when(recipeRepository.findById(anyString()))
                .thenReturn(Optional.of(new Recipe("1", "Test", "Test instruction", true, 1, Set.of(Ingredient.builder().name("meat").build()), System.currentTimeMillis())));

        val recipe = recipeService.getRecipeById("1");
        assertTrue(recipe.isPresent());
        assertEquals("Test", recipe.get().getName());
    }

    @Test
    void addRecipe() {
        val recipe = new Recipe(null, "Test", "Test instruction", true, 1, Set.of(Ingredient.builder().name("meat").build()), System.currentTimeMillis());
        Mockito.when(recipeRepository.save(recipe)).thenReturn(recipe);
        val recipeDto = RecipeDto.builder()
                .name("Test")
                .instruction("Test instruction")
                .vegetarian(true)
                .numberOfServings(1)
                .ingredients(List.of(Ingredient.builder().name("meat").build()))
                .build();
        recipeService.addRecipe(recipeDto);
        verify(recipeRepository, times(1)).save(any());
    }

    @Test
    void updateRecipe() {
        val recipe = new Recipe("1", "Test", "Test instruction", true, 1, Set.of(Ingredient.builder().name("meat").build()), System.currentTimeMillis());
        Mockito.when(recipeRepository.save(recipe)).thenReturn(recipe);
        val recipeDto = RecipeDto.builder()
                .id("1")
                .name("Test")
                .instruction("Test instruction")
                .vegetarian(true)
                .numberOfServings(1)
                .ingredients(List.of(Ingredient.builder().name("meat").build()))
                .build();
        recipeService.updateRecipe(recipeDto);
        verify(recipeRepository, times(1)).save(any());
    }

    @Test
    void deleteRecipe() {
        Mockito.doNothing().when(recipeRepository).deleteById(anyString());
        recipeService.deleteRecipe("1");
        verify(recipeRepository, times(1)).deleteById(anyString());
    }

    @Test
    void search() {
        Mockito.when(recipeSearchRepository.search(any())).thenReturn(List.of());
        recipeService.search(RecipeQuery.builder().build());
        verify(recipeSearchRepository, times(1)).search(any());
    }
}