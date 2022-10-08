package com.bruno.mypub.drinks.model.decorator;

import com.bruno.mypub.drinks.repository.entities.DrinkEntity;
import com.bruno.mypub.ingredients.model.entities.IngredientEntity;
import com.bruno.mypub.ingredients.model.response.IngredientsServiceResponse;

import java.util.List;

public class SpecialDrink extends DrinkDecorator {

    private final List<IngredientsServiceResponse> ingredients;

    public SpecialDrink(Drink drink, List<IngredientsServiceResponse> ingredients) {
        super(drink);
        this.ingredients = ingredients;
    }

    @Override
    public DrinkEntity assemble(DrinkEntity drinkEntity) {

        DrinkEntity drink = super.assemble(drinkEntity);
        drink.setName(drink.getName() + " com " + ingredients.stream().map(IngredientsServiceResponse::getName).reduce((a, b) -> a + ", " + b).orElse(""));
        drink.setCost(drink.getCost() + ingredients.stream().mapToDouble(IngredientsServiceResponse::getPrice).sum());
        return drink;
    }
}