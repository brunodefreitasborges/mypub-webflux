package com.bruno.mypub.ingredients.model.response;

import com.bruno.mypub.common.enums.IngredientCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientsServiceResponse {

    private String id;
    private IngredientCategory category;
    private String name;
    private Double price;
    private Double abv;
    private Integer quantity;

}
