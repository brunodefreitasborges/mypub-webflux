package com.bruno.mypub.ingredients.model.request;

import com.bruno.mypub.common.enums.IngredientCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientsServiceRequest {

    private IngredientCategory category;
    private String name;
    private Double price;
    private Integer quantity;
    private Double abv;

}
