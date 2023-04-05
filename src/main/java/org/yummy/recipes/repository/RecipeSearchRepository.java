package org.yummy.recipes.repository;

import lombok.val;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;
import org.yummy.recipes.dto.RecipeQuery;
import org.yummy.recipes.entity.Recipe;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Search recipe repository
 */
@Repository
public class RecipeSearchRepository {

    private final ElasticsearchOperations elasticsearchOperations;
    private final static String INDEX = "recipe";

    public RecipeSearchRepository(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    /**
     * Search recipes by structured query request
     *
     * @param recipeQuery structured query request
     * @return collection of recipes that match the request
     */
    public List<Recipe> search(RecipeQuery recipeQuery){
        val queryBuilder = QueryBuilders.boolQuery();

        if (recipeQuery.getVegetarian() != null) {
            queryBuilder.must(QueryBuilders.termQuery("vegetarian", recipeQuery.getVegetarian()));
        }
        if (recipeQuery.getNumberOfServings() != null){
            queryBuilder.must(QueryBuilders.termQuery("numberOfServings", recipeQuery.getNumberOfServings()));
        }
        if (recipeQuery.getInstruction() != null){

            //Removing wildcard characters from initial keyword
            val keyword = recipeQuery.getInstruction().toLowerCase().replace("*", "").replace("?", "");
            queryBuilder.must(QueryBuilders.wildcardQuery("instruction", String.format("%s*", keyword)));
        }
        if (recipeQuery.getIncludedIngredients() != null){
            recipeQuery.getIncludedIngredients().forEach( i -> queryBuilder.must(QueryBuilders.nestedQuery("ingredients", QueryBuilders.termQuery("ingredients.name", i.getName().toLowerCase()), ScoreMode.None)));
        }
        if (recipeQuery.getExcludedIngredients() != null){
            recipeQuery.getExcludedIngredients().forEach( i -> queryBuilder.mustNot(QueryBuilders.nestedQuery("ingredients", QueryBuilders.termQuery("ingredients.name", i.getName().toLowerCase()), ScoreMode.None)));
        }

        return elasticsearchOperations.search(new NativeSearchQueryBuilder()
                        .withQuery(queryBuilder)
                        .withPageable(PageRequest.of(recipeQuery.getPageNumber(), recipeQuery.getPageSize(), Sort.by("creationTime").descending()))
                        .build(), Recipe.class, IndexCoordinates.of(INDEX))
                .get()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
