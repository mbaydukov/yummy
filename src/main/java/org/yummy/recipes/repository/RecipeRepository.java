package org.yummy.recipes.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.yummy.recipes.entity.Recipe;

/**
 * CRUD recipe repository
 */
@Repository
public interface RecipeRepository extends ElasticsearchRepository<Recipe, String> {

}
