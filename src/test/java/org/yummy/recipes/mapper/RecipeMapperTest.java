package org.yummy.recipes.mapper;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.yummy.recipes.dto.Ingredient;
import org.yummy.recipes.dto.RecipeDto;
import org.yummy.recipes.entity.Recipe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
class RecipeMapperTest {

    @Test
    void entityToDto() {
        RecipeMapper mapper = RecipeMapper.INSTANCE;
        val entity = new Recipe("1", "Test", "Test instruction", true, 2, Set.of(Ingredient.builder().name("meat").build()), System.currentTimeMillis());
        val dto = mapper.map(entity);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getInstruction(), dto.getInstruction());
        assertEquals(entity.getVegetarian(), dto.getVegetarian());
        assertEquals(entity.getNumberOfServings(), dto.getNumberOfServings());
        assertEquals(entity.getIngredients(), new HashSet<>(dto.getIngredients()));
    }

    @Test
    void dtoToEntity() {
        RecipeMapper mapper = RecipeMapper.INSTANCE;
        val dto = RecipeDto.builder()
                .id("1")
                .name("Test")
                .instruction("Test instruction")
                .vegetarian(true)
                .numberOfServings(2)
                .ingredients(List.of(Ingredient.builder().name("meat").build()))
                .build();
        val entity = mapper.map(dto);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getInstruction(), entity.getInstruction());
        assertEquals(dto.getVegetarian(), entity.getVegetarian());
        assertEquals(dto.getNumberOfServings(), entity.getNumberOfServings());
        assertEquals(new HashSet<>(dto.getIngredients()), entity.getIngredients());
    }

    @Test
    void toCollectionOfDto() {
        RecipeMapper mapper = RecipeMapper.INSTANCE;
        val entities = List.of(new Recipe("1", "Test", "Test instruction", true, 2, Set.of(Ingredient.builder().name("meat").build()), System.currentTimeMillis()));
        val dtos = mapper.map(entities);
        assertEquals(entities.size(), dtos.size());

        val entity = entities.get(0);
        val dto = dtos.get(0);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getInstruction(), dto.getInstruction());
        assertEquals(entity.getVegetarian(), dto.getVegetarian());
        assertEquals(entity.getNumberOfServings(), dto.getNumberOfServings());
        assertEquals(entity.getIngredients(), new HashSet<>(dto.getIngredients()));
    }
}