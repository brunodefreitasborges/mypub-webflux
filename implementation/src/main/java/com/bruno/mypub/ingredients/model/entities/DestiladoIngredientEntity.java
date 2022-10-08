package com.bruno.mypub.ingredients.model.entities;

import com.bruno.mypub.common.enums.IngredientCategory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document(collection = "destilados")
public class DestiladoIngredientEntity extends IngredientEntity {

    private Double abv;

    public DestiladoIngredientEntity(
            String id,
            IngredientCategory category,
            String name,
            Double price,
            Integer stock,
            Double abv) {
        super(id, category, name, price, stock);
        this.abv = abv;
    }

}
