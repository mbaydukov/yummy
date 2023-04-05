package org.yummy.recipes.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.yummy.recipes.entity.Recipe;
import org.yummy.recipes.dto.RecipeDto;

import java.util.List;

/**
 * Provides mapping between Recipe entity and dto
 */
@Mapper
public interface RecipeMapper {

    RecipeMapper INSTANCE = Mappers.getMapper(RecipeMapper.class);

    RecipeDto map(Recipe recipe);

    @Mapping(source = "id", target = "creationTime", qualifiedByName = "creationTime")
    Recipe map(RecipeDto recipeDto);

    @Named("creationTime")
    static long creationTime(String id){
        return System.currentTimeMillis();
    }

    List<RecipeDto> map(List<Recipe> recipes);
}
