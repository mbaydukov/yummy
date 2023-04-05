package org.yummy.recipes.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * Open API configuration
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Yummy Recipes"))
public class OpenAPIConfig {

}

