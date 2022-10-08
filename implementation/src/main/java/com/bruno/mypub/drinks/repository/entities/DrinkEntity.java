package com.bruno.mypub.drinks.repository.entities;

import com.bruno.mypub.drinks.model.decorator.Drink;
import com.bruno.mypub.ingredients.model.entities.IngredientEntity;
import com.bruno.mypub.ingredients.model.response.IngredientsServiceResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "drinks")
public class DrinkEntity implements Drink {

    @Id
    private String id;
    private String name;
    private Double cost;
    private Double abv;
    private List<IngredientsServiceResponse> ingredients;
    private String preparation;
    private String glass;
    private String garnish;

    public static DrinkEntityBuilder builder() {
        return new DrinkEntityBuilder();
    }

    @Override
    public DrinkEntity assemble(DrinkEntity drinkEntity) {
        return drinkEntity;
    }

    // Builder For The Drink Entity Class >>>
    public static class DrinkEntityBuilder {

        // Attributes for the Drink Entity Class
        private String id;
        private String name;
        private Double cost;
        private Double abv;
        private List<IngredientsServiceResponse> ingredients;
        private String preparation;
        private String glass;
        private String garnish;



        // Empty Builder Constructor
        public DrinkEntityBuilder() {
        }

        // All Args Builder Constructor
        public DrinkEntityBuilder(DrinkEntity drinkEntity) {
            this.id = drinkEntity.getId();
            this.name = drinkEntity.getName();
            this.ingredients = drinkEntity.getIngredients();
            this.preparation = drinkEntity.getPreparation();
            this.glass = drinkEntity.getGlass();
            this.garnish = drinkEntity.getGarnish();
            this.cost = drinkEntity.getCost();
            this.abv = drinkEntity.getAbv();
        }

        // Single Argument Builder Constructor
        public DrinkEntityBuilder id(String id) {
            this.id = id;
            return this;
        }

        // Single Argument Builder Constructor
        public DrinkEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        // Single Argument Builder Constructor
        public DrinkEntityBuilder ingredients(List<IngredientsServiceResponse> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        // Single Argument Builder Constructor
        public DrinkEntityBuilder preparation(String preparation) {
            this.preparation = preparation;
            return this;
        }

        // Single Argument Builder Constructor
        public DrinkEntityBuilder glass(String glass) {
            this.glass = glass;
            return this;
        }

        // Single Argument Builder Constructor
        public DrinkEntityBuilder garnish(String garnish) {
            this.garnish = garnish;
            return this;
        }

        // Single Argument Builder Constructor
        public DrinkEntityBuilder cost(Double cost) {
            this.cost = cost;
            return this;
        }

        // Single Argument Builder Constructor
        public DrinkEntityBuilder abv(Double abv) {
            this.abv = abv;
            return this;
        }

        // Method to actually build the new Object with Specific Parameters
        public DrinkEntity build() {
            DrinkEntity drinkEntity = new DrinkEntity();
            drinkEntity.setId(id);
            drinkEntity.setName(name);
            drinkEntity.setIngredients(ingredients);
            drinkEntity.setPreparation(preparation);
            drinkEntity.setGlass(glass);
            drinkEntity.setGarnish(garnish);
            drinkEntity.setCost(cost);
            drinkEntity.setAbv(abv);

            return drinkEntity;
        }

    }
}
