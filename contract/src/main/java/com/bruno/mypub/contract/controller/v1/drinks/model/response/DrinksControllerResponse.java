package com.bruno.mypub.contract.controller.v1.drinks.model.response;

import com.bruno.mypub.contract.controller.v1.ingredients.model.response.IngredientsControllerResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DrinksControllerResponse {

    private String id;
    private String name;
    private Double cost;
    private Double abv;
    private List<IngredientsControllerResponse> ingredients;
    private String preparation;
    private String glass;
    private String garnish;

}
