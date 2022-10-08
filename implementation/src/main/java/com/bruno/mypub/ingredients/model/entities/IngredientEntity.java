package com.bruno.mypub.ingredients.model.entities;

import com.bruno.mypub.common.enums.IngredientCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class IngredientEntity {

    private String id;
    private IngredientCategory category;
    private String name;
    private Double price;
    private Integer stock;

    protected IngredientEntity(
            String id,
            IngredientCategory category,
            String name,
            Double price,
            Integer stock) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
}
