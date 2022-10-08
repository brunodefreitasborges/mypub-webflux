package com.bruno.mypub.drinks.model.response;

import com.bruno.mypub.ingredients.model.response.IngredientsServiceResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DrinksServiceResponse {

    private String id;
    private String name;
    private Double cost;
    private Double abv;
    private List<IngredientsServiceResponse> ingredients;
    private String preparation;
    private String glass;
    private String garnish;

}
