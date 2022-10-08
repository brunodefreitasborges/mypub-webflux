package com.bruno.mypub.contract.controller.v1.ingredients.model.response;

import com.bruno.mypub.common.enums.IngredientCategory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class IngredientsControllerResponse {

    private String id;
    private IngredientCategory category;
    private String name;
    private Double price;
    private Double abv;
    private Integer quantity;

}
