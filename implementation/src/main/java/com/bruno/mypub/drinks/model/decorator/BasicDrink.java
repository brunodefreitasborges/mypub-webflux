package com.bruno.mypub.drinks.model.decorator;

import com.bruno.mypub.drinks.repository.entities.DrinkEntity;

public class BasicDrink implements Drink{

    @Override
    public DrinkEntity assemble(DrinkEntity drinkEntity) {
        return drinkEntity;
    }
}
