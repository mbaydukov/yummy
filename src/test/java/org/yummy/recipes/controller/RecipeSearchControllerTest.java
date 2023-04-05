package org.yummy.recipes.controller;

import lombok.val;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.yummy.recipes.YummyRecipes;
import org.yummy.recipes.config.Application;
import org.yummy.recipes.dto.Ingredient;
import org.yummy.recipes.dto.RecipeDto;
import org.yummy.recipes.dto.RecipeQuery;
import org.yummy.recipes.repository.RecipeRepository;
import org.yummy.recipes.service.RecipeService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = YummyRecipes.class)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Tag("IntegrationTest")
class RecipeSearchControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private Application app;

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private RecipeRepository recipeRepository;

    @Container
    private static ElasticsearchContainer container = new ElasticsearchContainer(
            DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:7.6.2"))
            .withReuse(false);

    @DynamicPropertySource
    private static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", () -> "http://" + container.getHttpHostAddress());
    }

    @BeforeEach
    public void setup() {
        val recipes = List.of(
                RecipeDto.builder()
                .name("For vegans")
                .instruction("Cook veg dish for two")
                .vegetarian(true)
                .numberOfServings(2)
                .ingredients(List.of(Ingredient.builder().name("RICE").build()))
                .build(),
                RecipeDto.builder()
                        .name("For meat lovers")
                        .instruction("Let's make BBQ")
                        .vegetarian(false)
                        .numberOfServings(1)
                        .ingredients(List.of(Ingredient.builder().name("meat").build()))
                        .build(),
                RecipeDto.builder()
                        .name("Traditional")
                        .instruction("Meat with carrot")
                        .vegetarian(false)
                        .ingredients(List.of(Ingredient.builder().name("meat").build(), Ingredient.builder().name("carrot").build()))
                        .build(),
                RecipeDto.builder()
                        .name("Something interesting")
                        .instruction("Let's cooking Something Interesting")
                        .numberOfServings(3)
                        .ingredients(List.of(Ingredient.builder().name("SUGAR").build(), Ingredient.builder().name("milk").build()))
                        .build()
                );
        recipeRepository.deleteAll();
        recipes.forEach(r -> recipeService.addRecipe(r));
    }

    @Test
    void searchVegetarianWithRice() {
        val recipes = search(RecipeQuery.builder()
                .vegetarian(true)
                .includedIngredients(List.of(Ingredient.builder().name("rice").build()))
                .build());
        assertEquals(1, recipes.size());
        assertTrue(recipes.stream().findFirst().isPresent());
        assertEquals("For vegans", recipes.stream().findFirst().get().getName());
    }

    @Test
    void searchNonVegetarianWithMeat() {
        val recipes = search(RecipeQuery.builder()
                .vegetarian(false)
                .includedIngredients(List.of(Ingredient.builder().name("meat").build()))
                .build());
        assertEquals(2, recipes.size());
    }

    @Test
    void searchNonVegetarianWithoutMeat() {
        val recipes = search(RecipeQuery.builder()
                .vegetarian(false)
                .excludedIngredients(List.of(Ingredient.builder().name("meat").build()))
                .build());
        assertEquals(0, recipes.size());
    }

    @Test
    void searchForPersons() {
        val recipes = search(RecipeQuery.builder()
                .numberOfServings(3)
                .build());
        assertEquals(1, recipes.size());
    }

    @Test
    void searchWildcard() {
        val recipes = search(RecipeQuery.builder()
                .instruction("b?b*Q")
                .pageNumber(0)
                .pageSize(1000)
                .build());
        assertEquals(1, recipes.size());
    }

    @Test
    void incorrectPageNumber() {
        app.testClient(port)
                .post()
                .uri("/api/recipes/search")
                .bodyValue(RecipeQuery.builder()
                        .pageNumber(-1)
                        .build())
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    void incorrectPageSize() {
        app.testClient(port)
                .post()
                .uri("/api/recipes/search")
                .bodyValue(RecipeQuery.builder()
                        .pageSize(10_000)
                        .build())
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    void pageSize() {
        val result = search(RecipeQuery.builder()
                .pageSize(1000)
                .build());
        assertTrue(result.size() > 1);

        val limitedResult = search(RecipeQuery.builder()
                .pageSize(1)
                .build());
        assertEquals(1, limitedResult.size());
    }

    private List<RecipeDto> search(RecipeQuery recipeQuery){
        return Arrays.asList(Objects.requireNonNull(app.testClient(port)
                .post()
                .uri("/api/recipes/search")
                .bodyValue(recipeQuery)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RecipeDto[].class)
                .returnResult()
                .getResponseBody()));
    }
}