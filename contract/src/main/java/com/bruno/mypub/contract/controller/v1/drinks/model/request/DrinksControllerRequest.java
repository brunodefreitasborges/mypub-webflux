package com.bruno.mypub.contract.controller.v1.drinks.model.request;

import com.bruno.mypub.contract.controller.v1.ingredients.model.request.IngredientsControllerRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DrinksControllerRequest {

    @NotBlank(message = "Drink Name cannot be empty")
    private String name;
    @Valid
    private List<IngredientsControllerRequest> ingredients;
    @NotBlank(message = "Preparation cannot be empty")
    private String preparation;
    @NotBlank(message = "Glass cannot be empty")
    private String glass;
    @Builder.Default
    private String garnish = "None";

}
