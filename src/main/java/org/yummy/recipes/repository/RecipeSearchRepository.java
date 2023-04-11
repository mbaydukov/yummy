package org.yummy.recipes.repository;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.elc.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
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
     * @return collection of recipes that match the request. The result collection is sorted by Recipe.creationTime parameter in descending order
     */
    public List<Recipe> search(RecipeQuery recipeQuery){
        val queryBuilder = QueryBuilders.matchAllQueryAsQuery().bool();

        if (recipeQuery.getVegetarian() != null) {
            queryBuilder.must().add(TermQuery.of(b -> b.field("numberOfServings").value(recipeQuery.getVegetarian()))._toQuery());
        }
        if (recipeQuery.getNumberOfServings() != null){
            queryBuilder.must().add(TermQuery.of(b -> b.field("numberOfServings").value(recipeQuery.getNumberOfServings()))._toQuery());
        }
        if (recipeQuery.getInstruction() != null){
            queryBuilder.must().add(MatchQuery.of(b -> b.field("instruction").analyzer(recipeQuery.getInstruction()).operator(Operator.Or))._toQuery());
        }
        if (recipeQuery.getIncludedIngredients() != null){
            recipeQuery.getIncludedIngredients().forEach( i -> queryBuilder.must().add(NestedQuery.of(v -> v.path("ingredients")
                    .query(QueryBuilders.termQuery("ingredients.name", i.getName())._toQuery())
                    .scoreMode(ChildScoreMode.None))._toQuery()));
        }
        if (recipeQuery.getExcludedIngredients() != null){
            recipeQuery.getIncludedIngredients().forEach( i -> queryBuilder.mustNot().add(NestedQuery.of(v -> v.path("ingredients")
                    .query(QueryBuilders.termQuery("ingredients.name", i.getName())._toQuery())
                    .scoreMode(ChildScoreMode.None))._toQuery()));
        }

        return elasticsearchOperations.search(new NativeQueryBuilder()
                        .withQuery(queryBuilder._toQuery())
                        .withPageable(PageRequest.of(recipeQuery.getPageNumber(), recipeQuery.getPageSize(), Sort.by("creationTime").descending()))
                        .build(), Recipe.class, IndexCoordinates.of(INDEX))
                .get()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
