package com.emailmicroservice.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrinkModel {

    private String id;
    private String name;
    private Double cost;
    private Double abv;
    private List<? extends IngredientModel> ingredients;
    private String preparation;
    private String glass;
    private String garnish;
}
