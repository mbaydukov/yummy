package org.yummy.recipes.controller;

import lombok.val;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.yummy.recipes.YummyRecipes;
import org.yummy.recipes.config.Application;
import org.yummy.recipes.dto.Ingredient;
import org.yummy.recipes.dto.RecipeDto;
import org.yummy.recipes.repository.RecipeRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = YummyRecipes.class)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Tag("IntegrationTest")
class RecipeManagementControllerTest {

    @LocalServerPort
    private int port;

    @Container
    private static final ElasticsearchContainer container = new ElasticsearchContainer(
            DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:7.6.2")) //
            .withReuse(true);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", () -> "http://" + container.getHttpHostAddress());
    }

    @Autowired
    private Application app;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    void recipeNotFound(){
        app.testClient(port)
                .get()
                .uri("/api/recipes/{recipeId}", UUID.randomUUID().toString())
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void getRecipeById() {

        val newRecipe = getRecipe("Test Recipe", "Let's cooking Test Recipe");
        val persistedRecipe = persistRecipe(newRecipe);

        val fetchedRecipe = app.testClient(port)
                .get()
                .uri("/api/recipes/{recipeId}", persistedRecipe.getId())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RecipeDto.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(fetchedRecipe);
        assertNotNull(fetchedRecipe.getId());
        assertEquals(newRecipe.getName(), fetchedRecipe.getName());
        assertEquals(newRecipe.getInstruction(), fetchedRecipe.getInstruction());
        assertEquals(newRecipe.getVegetarian(), fetchedRecipe.getVegetarian());
        assertEquals(newRecipe.getNumberOfServings(), fetchedRecipe.getNumberOfServings());

        val r = recipeRepository.findAll();
    }

    @Test
    void addRecipe() {
        val newRecipe = getRecipe("Test Recipe", "Let's cooking Test Recipe");
        val persistedRecipe = persistRecipe(newRecipe);

        assertNotNull(persistedRecipe);
        assertNotNull(persistedRecipe.getId());
        assertEquals(newRecipe.getName(), persistedRecipe.getName());
        assertEquals(newRecipe.getInstruction(), persistedRecipe.getInstruction());
        assertEquals(newRecipe.getVegetarian(), persistedRecipe.getVegetarian());
        assertEquals(newRecipe.getNumberOfServings(), persistedRecipe.getNumberOfServings());
    }

    @Test
    void addRecipeWithSpecifiedId() {
        val newRecipe = RecipeDto.builder()
                .id("id")
                .name("Test Recipe")
                .instruction("Let's cooking Test Recipe")
                .vegetarian(false)
                .numberOfServings(4)
                .ingredients(List.of(
                        Ingredient.builder().name("Rise").build(),
                        Ingredient.builder().name("Meat").build()
                ))
                .build();
        assertThrows(AssertionError.class, () -> persistRecipe(newRecipe));
    }

    @Test
    void updateRecipe() {
        val persistedRecipe = persistRecipe(getRecipe("Test Recipe", "Let's cooking Test Recipe"));
        persistedRecipe.setName("Test Recipe 2");
        persistedRecipe.setInstruction("Let's cooking Test Recipe 2");
        persistedRecipe.setVegetarian(true);
        persistedRecipe.setNumberOfServings(5);
        persistedRecipe.setIngredients(List.of(
                Ingredient.builder().name("Rise 2").build(),
                Ingredient.builder().name("Meat 2").build()
        ));

        val updatedRecipe = app.testClient(port)
                .patch()
                .uri("/api/recipes")
                .bodyValue(persistedRecipe)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RecipeDto.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(updatedRecipe);
        assertNotNull(persistedRecipe.getId());
        assertEquals(persistedRecipe.getName(), updatedRecipe.getName());
        assertEquals(persistedRecipe.getInstruction(), updatedRecipe.getInstruction());
        assertEquals(persistedRecipe.getVegetarian(), updatedRecipe.getVegetarian());
        assertEquals(persistedRecipe.getNumberOfServings(), updatedRecipe.getNumberOfServings());
    }

    @Test
    void delete() {
        val persistedRecipe = persistRecipe(getRecipe("Test Recipe", "Let's cooking Test Recipe"));
        app.testClient(port)
                .delete()
                .uri("/api/recipes/{recipeId}", persistedRecipe.getId())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    private RecipeDto getRecipe(String name, String instruction){
        return RecipeDto.builder()
                .name(name)
                .instruction(instruction)
                .vegetarian(false)
                .numberOfServings(4)
                .ingredients(List.of(
                        Ingredient.builder().name("Rise").build(),
                        Ingredient.builder().name("Meat").build()
                ))
                .build();
    }

    private RecipeDto persistRecipe(RecipeDto newRecipe){
        return app.testClient(port)
                .post()
                .uri("/api/recipes")
                .bodyValue(newRecipe)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RecipeDto.class)
                .returnResult()
                .getResponseBody();
    }
}