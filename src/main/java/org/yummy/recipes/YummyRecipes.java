package org.yummy.recipes;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class YummyRecipes {

    /**
     * Create a standalone java application which allows users to manage their favourite recipes. It should
     * allow adding, updating, removing and fetching recipes. Additionally users should be able to filter
     * available recipes based on one or more of the following criteria:
     * <p>
     * 1. Whether or not the dish is vegetarian
     * 2. The number of servings
     * 3. Specific ingredients (either include or exclude)
     * 4. Text search within the instructions.
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(YummyRecipes.class).run(args);
    }
}
