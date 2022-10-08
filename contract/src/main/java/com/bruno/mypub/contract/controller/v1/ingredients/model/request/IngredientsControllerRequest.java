package com.bruno.mypub.contract.controller.v1.ingredients.model.request;

import com.bruno.mypub.common.enums.IngredientCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientsControllerRequest {


    private IngredientCategory category;
    @NotBlank(message = "Ingredient Name cannot be empty")
    private String name;
    @Builder.Default
    private Double price = 0.00;
    @Builder.Default
    private Integer quantity = 0;
    @Builder.Default
    private Double abv = null;

}
