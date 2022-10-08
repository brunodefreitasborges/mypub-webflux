package com.bruno.mypub.drinks.model.request;

import com.bruno.mypub.ingredients.model.request.IngredientsServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DrinksServiceRequest {

    private String name;
    private List<IngredientsServiceRequest> ingredients;
    private String preparation;
    private String glass;
    private String garnish;

}
