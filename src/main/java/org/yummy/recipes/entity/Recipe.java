package org.yummy.recipes.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.yummy.recipes.dto.Ingredient;

import java.util.Set;


/**
 * Recipe document
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "recipe")
public class Recipe {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String instruction;

    @Field(type = FieldType.Boolean)
    private Boolean vegetarian;

    @Field(type = FieldType.Integer)
    private Integer numberOfServings;

    @Field(type = FieldType.Nested)
    private Set<Ingredient> ingredients;

    @Field(type = FieldType.Long)
    private Long creationTime;
}
