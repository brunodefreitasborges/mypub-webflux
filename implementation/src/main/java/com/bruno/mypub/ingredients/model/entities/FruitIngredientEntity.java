package com.bruno.mypub.ingredients.model.entities;

import com.bruno.mypub.common.enums.IngredientCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "fruits")
public class FruitIngredientEntity extends IngredientEntity{


    public FruitIngredientEntity(
            String id,
            IngredientCategory category,
            String name,
            Double price,
            Integer stock) {
        super(id, category, name, price, stock);
    }




}
