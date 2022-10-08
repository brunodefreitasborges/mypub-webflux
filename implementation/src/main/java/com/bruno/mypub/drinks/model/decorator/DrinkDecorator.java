package com.bruno.mypub.drinks.model.decorator;

import com.bruno.mypub.drinks.repository.entities.DrinkEntity;

public class DrinkDecorator {

    protected final Drink drink;

    public DrinkDecorator(Drink drink) {
        this.drink = drink;
    }

    public DrinkEntity assemble(DrinkEntity drinkEntity) {
        return this.drink.assemble(drinkEntity);
    }
}
